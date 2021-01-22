# code-coverage-remote
Generate Code Coverage for Remote JVM

## Enabling Jacoco agent for Remote JVM(Server)

```bash
-javaagent:/usr/local/tomcat/lib/jacocoagent.jar=port=6300,address=0.0.0.0,destfile=/tmp/jacoco-remote.exec,includes=com.sample.*,append=true,output=tcpserver
```

##Tomcat Server

The extended tomcat server on the docker container is already enabled with Jacoco plugin and sample application for quick remote coverage testing

```bash

clone https://github.com/techforum-repo/docker-projects/tree/master/tomcat-extended

docker-compose up

```
##Manual Test the application on server

http://localhost:8080/maven-sample-webapp/sample?input=test

If required integration test cases can be enabled to test the sample application deployed on tomcat server

## Generate report

```bash
mvn clean verify sonar:sonar
```

The coverage report is generated under target/site/jacoco, view the report by opening index.html
The coverage report is also uploaded to sonarqube, the report can be viewed from sonarqube once ready.