# Use a Java 21 base image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY myapp.jar .

# Expose port 8080
EXPOSE 8080

# Command to run the JAR file
CMD ["java", "-jar", "myapp.jar"]
