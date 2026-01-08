FROM eclipse-temurin:17-jdk-alpine

RUN mkdir -p /opt/i27

WORKDIR /opt/i27

ARG JAR_SOURCE

COPY ["${JAR_SOURCE}", "/opt/i27/i27-eureka.jar"]

CMD ["java", "-jar", "/opt/i27/i27-eureka.jar"]