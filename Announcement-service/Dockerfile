FROM openjdk
EXPOSE 8082
ADD target/announcement-service.jar announcement-service.jar
ENTRYPOINT ["java", "-jar", "announcement-service.jar"]