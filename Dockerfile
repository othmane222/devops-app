FROM maven:3.9.8-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src


RUN mvn package -DskipTests



FROM eclipse-temurin:17-jre-focal

WORKDIR /app

COPY --from=build /app/target/devops-demo-app-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8080


ENTRYPOINT ["java", "-jar", "app.jar"]