FROM gradle:8.10.1-jdk21 AS build
WORKDIR /app
COPY . .
RUN sh ./gradlew :apps:bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/apps/build/libs/moderation-service.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]