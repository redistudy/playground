FROM openjdk:17-slim-buster

ARG APP_VERSION
COPY build/libs/filebeat-test-$APP_VERSION.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
