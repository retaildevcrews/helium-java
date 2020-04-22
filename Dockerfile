# ----- Base Java - Check Dependencies ----
FROM maven:3.6.1-jdk-8 AS base
ENV HOME=/app
WORKDIR $HOME
ADD pom.xml $HOME

# Pull the app insights jar into the docker image as it is still in public preview
RUN  wget -O /app/applicationinsights-agent-3.0.0-PREVIEW.2.jar https://github.com/microsoft/ApplicationInsights-Java/releases/download/3.0.0-PREVIEW.2/applicationinsights-agent-3.0.0-PREVIEW.2.jar

#
# ----Build App with Dependencies ----
FROM base AS dependencies
RUN mvn verify clean --fail-never
ADD . $HOME
RUN mvn clean package -DskipTests

#
# ---- Release App ----
FROM  openjdk:8-jre-alpine AS release
ARG build_ver
ENV HOME=/app
WORKDIR $HOME
# Create a user
RUN addgroup -g 4120 -S helium && \
    adduser -u 4120 -S helium -G helium
USER helium

#Note: Every time we update helium version, we must update the jar version below

COPY --from=dependencies /app/target/helium-0.1.0.jar app.jar
COPY --from=dependencies /app/applicationinsights-agent-3.0.0-PREVIEW.2.jar applicationinsights-agent-3.0.0-PREVIEW.2.jar
EXPOSE 4120
CMD ["java", "-javaagent:./applicationinsights-agent-3.0.0-PREVIEW.2.jar", "-jar", "./app.jar"]
