FROM eclipse-temurin:17-jdk-alpine
MAINTAINER udipta.com
COPY target/OpetelemetryBancs-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080