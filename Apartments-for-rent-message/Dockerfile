FROM openjdk
EXPOSE 8083
ADD target/message-service.jar message-service.jar
ENTRYPOINT ["java", "-jar", "message-service.jar"]