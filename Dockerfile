# ----- Base Java - Check Dependencies ----
FROM maven:3.6.1-jdk-8 AS base
WORKDIR /app
ADD pom.xml /app

# Pull the app insights jar into the docker image as it is still in public preview
RUN  wget -O $APPDIR/applicationinsights-agent-3.0.0-PREVIEW.2.jar https://github.com/microsoft/ApplicationInsights-Java/releases/download/3.0.0-PREVIEW.2/applicationinsights-agent-3.0.0-PREVIEW.2.jar

#
# ----Build App with Dependencies ----
FROM base AS dependencies
ADD . /app

# While creating the docker container as part of the CI/CD, the managed identity is not set
# and thus many tests will fail, so skipping during docker build. Instead they will run as part
# of the CI/CD.
RUN mvn clean package -DskipTests

#
# ---- Release App ----
FROM  openjdk:8-jre-alpine AS release
WORKDIR /app

# Create the helium user so we can run the app as non-root under helium
RUN addgroup -g 4120 -S helium && \
    adduser -u 4120 -S helium -G helium
USER helium

COPY --from=dependencies /app/target/helium-0.1.0.jar app.jar
COPY --from=dependencies /app/applicationinsights-agent-3.0.0-PREVIEW.2.jar applicationinsights-agent-3.0.0-PREVIEW.2.jar
EXPOSE 4120
CMD ["java", "-javaagent:/app/applicationinsights-agent-3.0.0-PREVIEW.2.jar", "-jar", "/app/app.jar"]
