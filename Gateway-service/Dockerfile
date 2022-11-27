FROM openjdk
EXPOSE 8010
ADD target/gateway-service.jar gateway-service.jar
ENTRYPOINT ["java", "-jar", "gateway-service.jar"]