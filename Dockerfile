# Build stage
FROM openjdk:21-jdk-slim AS builder

# Install Scala CLI
RUN apt-get update && \
    apt-get install -y curl gzip && \
    curl -fL https://github.com/VirtusLab/scala-cli/releases/latest/download/scala-cli-x86_64-pc-linux.gz | gzip -d > /usr/local/bin/scala-cli && \
    chmod +x /usr/local/bin/scala-cli

# Set workdir and files
WORKDIR /app
COPY . .

# Scala CLI config and compile server
RUN scala-cli clean .
RUN scala-cli config power true
RUN scala-cli package . -o -f scala-server.jar

# Prod stage
FROM gcr.io/distroless/java

# Set workdir and copy jar
WORKDIR /app
COPY --from=builder /app/scala-server.jar .

# Expose port
EXPOSE 8080

# Run server
CMD ["scala-server.jar"]