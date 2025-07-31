# ------------ Stage 1: Build the application ------------
FROM maven:3.9.3-eclipse-temurin-21-jammy AS build

# Set working directory
WORKDIR /app

# Copy only the pom.xml and download dependencies first (for caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Package the application (skip tests for faster builds)
RUN mvn clean package -DskipTests


# ------------ Stage 2: Run the application ------------
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/JobPortal-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot's default port
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
