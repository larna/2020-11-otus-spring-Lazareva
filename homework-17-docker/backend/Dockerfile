FROM openjdk:11-jre-slim
COPY /target/books-5.0-SNAPSHOT.jar /app/app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]