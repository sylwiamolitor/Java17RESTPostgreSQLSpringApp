FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/demo-0.0.4-SNAPSHOT.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]