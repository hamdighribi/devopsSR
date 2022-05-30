FROM openjdk:8-jdk-alpine
EXPOSE 8085
ADD target/*.war app.war
ENTRYPOINT ["java","-jar","app.war"]