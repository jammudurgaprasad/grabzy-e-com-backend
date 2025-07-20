# === STAGE 1: Build the application with Maven and Java 21 ===
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy all project files into the container
COPY . .

# Package the application (skip tests for faster builds)
RUN mvn clean package -DskipTests

# === STAGE 2: Run the packaged application with Java 21 ===
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app uses
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
