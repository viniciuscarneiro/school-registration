FROM openjdk:17-jdk-slim-buster
WORKDIR /school-registration-app
COPY . .
RUN ./gradlew clean build
CMD ./gradlew bootRun