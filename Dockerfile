FROM eclipse-temurin:21
EXPOSE 8080
ADD build/libs/AuthService*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]