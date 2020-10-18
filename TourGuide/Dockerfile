FROM openjdk:8-jdk-alpine
LABEL responsable="o.barberis@outlook.fr"
EXPOSE 8045:8080
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} tourguide.jar
ENTRYPOINT ["java","-jar","/tourguide.jar"]