
FROM gradle:8.5-jdk17-alpine AS build
WORKDIR /app


COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle.kts settings.gradle.kts ./
COPY src ./src


RUN chmod +x gradlew


RUN ./gradlew clean build -x test --no-daemon



FROM eclipse-temurin:17-jre-alpine
WORKDIR /app


COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
