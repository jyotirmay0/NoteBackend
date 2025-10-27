FROM gradle:8.10.2-jdk21-alpine AS build
WORKDIR /app

# Copy Gradle files first (for caching dependencies)
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY gradlew ./

# Copy the rest of the source code
COPY src ./src

# Give execution permission to Gradle wrapper
RUN chmod +x ./gradlew

# Build the project without running tests
RUN ./gradlew clean build -x test --no-daemon


# ====== 2️⃣ Run Stage (Lightweight JRE) ======
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
