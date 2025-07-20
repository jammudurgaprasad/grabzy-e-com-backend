# === STAGE 1: Build the application ===
FROM maven:3.8.5-openjdk-17 AS build

# Set working directory inside container
WORKDIR /app

# Copy all project files into the container
COPY . .

# Package the application (skip tests to speed up build)
RUN mvn clean package -DskipTests

# === STAGE 2: Run the application ===
FROM eclipse-temurin:17-jdk

# Set working directory in the runtime container
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app listens on (default 8080)
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "app.jar"]
