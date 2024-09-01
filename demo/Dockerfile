# Build stage
FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-21-jdk -y
RUN apt-get install maven -y

WORKDIR /app
COPY . .

# Ensure firebase-key.json is in the correct location before building
COPY src/main/resources/firebase-key.json /app/src/main/resources/

RUN mvn clean install

# Run stage
FROM openjdk:21-jdk-slim

EXPOSE 8080

WORKDIR /app

# Copy the built JAR file
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Copy the Firebase configuration file
COPY --from=build /app/src/main/resources/firebase-key.json /app/

# Set the working directory to where the JAR and config files are located
WORKDIR /app

ENTRYPOINT ["java", "-jar", "app.jar"]