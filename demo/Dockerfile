FROM openjdk:21-jdk-slim
# ARG JAR-FILE=target/*.jar
# ARG JAR-FILE=target/demo-0.0.1-SNAPSHOT.jar
COPY target/*.jar app.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "/app.jar"]