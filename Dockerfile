#
## Use an OpenJDK 17 base image
#FROM openjdk:17
#
## Set the working directory inside the container
#WORKDIR /usr/src/app
#COPY ./mvnw /usr/src/app
#COPY ./pom.xml /usr/src/app
#COPY ./.mvn /usr/src/app/.mvn
#RUN ./mvnw dependency:go-offline
#
## Copy the packaged JAR file into the container at the defined working directory
#COPY whatsapp-0.0.1-SNAPSHOT.jar /app/whatsapp-0.0.1-SNAPSHOT.jar
#
## Expose the port your application runs on
#EXPOSE 8080
#
## Specify the command to run your application
#CMD ["java", "-jar", "whatsapp-0.0.1-SNAPSHOT.jar"]


# Build stage
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /usr/src/app
COPY . /usr/src/app/
RUN mvn -B -DskipTests=true clean package

# Production stage
FROM openjdk:17-jdk-alpine
WORKDIR /usr/src/app
COPY --from=build /usr/src/app/target/whatsapp-0.0.1-SNAPSHOT.jar /usr/src/app/
EXPOSE 8081
ENTRYPOINT ["java","-jar","/usr/src/app/whatsapp-0.0.1-SNAPSHOT.jar"]
