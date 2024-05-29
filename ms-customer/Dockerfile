FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY /target/ms-customer-0.0.1-SNAPSHOT.jar customer-service.jar
EXPOSE 8070
ENTRYPOINT ["java", "-Xms2048m", "-Xmx2048m", "-jar","/customer-service.jar"]
