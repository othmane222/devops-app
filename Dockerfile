FROM eclipse-temurin:17-jre-focal
WORKDIR /app
COPY target/devops-demo-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]