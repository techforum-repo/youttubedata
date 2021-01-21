# maven-sample-webapp
Sample Web Application - Tomcat Server

## Building

```bash
mvn package
```

Produces `target/maven-sample-webapp.war` that can be deployed to Tomcat 
server.

## Deploying 

Application can be deployed to the server by navigating to http://<server>:8080/manager/html
and using the app-manager form for WAR file deployment.

After deploying the WAR file , navigate to the http://localhost:8080/maven-sample-webapp/ & http://localhost:8080/maven-sample-webapp/sample 
