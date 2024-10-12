# Build stage
FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-21-jdk -y
RUN apt-get install maven -y

WORKDIR /app
COPY . .

RUN mvn clean install

# Run stage
FROM openjdk:21-jdk-slim

EXPOSE 8080

WORKDIR /app

# Copy the built JAR file
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]