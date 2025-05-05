FROM openjdk:17-alpine
VOLUME /tmp
EXPOSE 8081
ADD ../CardManagementService/target/CardManagementService-0.0.1-SNAPSHOT.jar CardManagementService/server.jar
ENTRYPOINT ["java","-jar","CardManagementService/app.jar"]
