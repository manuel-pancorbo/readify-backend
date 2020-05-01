FROM gradle:6.3.0-jdk8 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar

FROM openjdk:8-jre-slim

EXPOSE 8000

ENV SPRING_PROFILES_ACTIVE=prod

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/readify-backend.jar

ENTRYPOINT ["java", "-jar","/app/readify-backend.jar"]