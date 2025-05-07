# Start from an OpenJDK 23 base image (builder stage)
FROM eclipse-temurin:23-jdk as builder

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn/ .mvn/
COPY pom.xml .

# Make the Maven wrapper executable
RUN chmod +x mvnw

# Copy source code
COPY src ./src

# Build the app (adjust "-DskipTests" if needed)
RUN ./mvnw clean package -DskipTests

# Second stage: run only the built app (runtime stage)
FROM eclipse-temurin:23-jdk

WORKDIR /app

# Copy the JAR built by the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port (adjust if your app uses a different port)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]