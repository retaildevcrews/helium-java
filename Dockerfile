# ----- Base Java - Check Dependencies ----
FROM maven:3.6.1-jdk-8 AS base
ENV HOME=/app
WORKDIR $HOME
ADD pom.xml $HOME

#
# ----Build App with Dependencies ----
FROM base AS dependencies
RUN mvn verify clean --fail-never
ADD . $HOME
RUN mvn clean package -DskipTests

#
# ---- Release App ----
FROM  openjdk:8-jre-alpine AS release
ENV HOME=/app
WORKDIR $HOME
# Create a user
RUN adduser -S appuser
USER appuser
COPY --from=dependencies /app/target/helium-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "./app.jar"]