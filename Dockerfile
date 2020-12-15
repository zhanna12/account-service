FROM openjdk:8
ADD target/account-service.jar account-service.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "account-service.jar"]