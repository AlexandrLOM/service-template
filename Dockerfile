FROM anapsix/alpine-java
MAINTAINER alexL
COPY target/service-template-0.0.1-SNAPSHOT.jar /home/service-template-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","/home/service-template-0.0.1-SNAPSHOT.jar"]