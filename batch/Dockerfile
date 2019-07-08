FROM openjdk:8-jre-alpine

VOLUME /tmp
ADD target/batch-k8-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-Xmx512m", "-jar", "/app/app.jar"]