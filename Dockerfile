# ----- Base Java - Check Dependencies ----
FROM maven:3.6.1-jdk-8 AS base
ENV APPDIR=/app
WORKDIR $APPDIR
ADD pom.xml $APPDIR

# Pull the app insights jar into the docker image as it is still in public preview
RUN  wget -O $APPDIR/applicationinsights-agent-3.0.0-PREVIEW.2.jar https://github.com/microsoft/ApplicationInsights-Java/releases/download/3.0.0-PREVIEW.2/applicationinsights-agent-3.0.0-PREVIEW.2.jar

#
# ----Build App with Dependencies ----
FROM base AS dependencies
RUN mvn verify clean --fail-never
ADD . $APPDIR
RUN mvn clean package -DskipTests

#
# ---- Release App ----
FROM  openjdk:8-jre-alpine AS release
ENV APPDIR=/app
WORKDIR $APPDIR

# Create a user
RUN addgroup -g 4120 -S helium && \
    adduser -u 4120 -S helium -G helium && \
    mkdir -p /home/helium && \
    chown -R helium:helium /home/helium
USER helium

#Note: Every time we update helium version, we must update the jar version below

COPY --from=dependencies $APPDIR/target/helium-0.1.0.jar app.jar
COPY --from=dependencies $APPDIR/applicationinsights-agent-3.0.0-PREVIEW.2.jar applicationinsights-agent-3.0.0-PREVIEW.2.jar
EXPOSE 4120
CMD ["java", "-javaagent:/app/applicationinsights-agent-3.0.0-PREVIEW.2.jar", "-jar", "/app/app.jar"]
