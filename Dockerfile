FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/OpetelemetryBancs-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080