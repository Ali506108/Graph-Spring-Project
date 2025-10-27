FROM gradle:8.7-jdk21 AS builder
WORKDIR /app

COPY settings.gradle.kts gradle.properties* ./
COPY gradle ./gradle
COPY build.gradle.kts ./
COPY src ./src

RUN gradle clean build

FROM openjdk:21-jdk AS runtime
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8791

ENTRYPOINT ["java", "-jar", "app.jar"]
