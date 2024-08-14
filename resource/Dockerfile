FROM maven:3.9.8-eclipse-temurin-22-alpine AS build

WORKDIR /app

COPY ./pom.xml .
COPY ./resource ./resource
COPY ./identity ./identity
COPY ./bff ./bff

RUN mvn clean install -DskipTests -pl resource

FROM openjdk:22-jdk

WORKDIR /app

COPY --from=build /app/resource/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]