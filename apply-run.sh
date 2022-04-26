#! /bin/bash

echo 'Clean app'
  mvn clean
echo 'Build app'
  mvn package
echo 'Run app'
  java -jar target/service-template-0.0.1-SNAPSHOT.jar
