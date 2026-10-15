# Stage 1: Build the application using Maven (match <java.version> in pom.xml)
FROM maven:3-eclipse-temurin-25 AS build
# For Java 25: FROM maven:3-eclipse-temurin-25 AS build
# For Java 26: FROM maven:3-eclipse-temurin-26 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
 
# Stage 2: Minimal runtime image for production
FROM eclipse-temurin:25-jre
# For Java 25: FROM eclipse-temurin:25-jre
# For Java 26: FROM eclipse-temurin:26-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
