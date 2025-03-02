# Use OpenJDK 21 image
FROM openjdk:21

# Set working directory
WORKDIR /app

# Copy Maven wrapper and project files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download project dependencies
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY src src

# Package the application
RUN ./mvnw package -DskipTests

# Expose port 8081 for the Spring Boot app
EXPOSE 9090

# Run the application
CMD ["java", "-jar", "target/uny-database-redmine-0.0.1-SNAPSHOT.war"]