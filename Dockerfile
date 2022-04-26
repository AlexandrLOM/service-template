FROM adoptopenjdk/openjdk11:latest
ADD target/service-template-0.0.1-SNAPSHOT.jar service-template-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "service-template-0.0.1-SNAPSHOT.jar"]