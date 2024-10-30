FROM eclipse-temurin:23-jdk-alpine AS build

WORKDIR /app

COPY .mvn/ ./.mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean install

FROM eclipse-temurin:23-alpine

WORKDIR /app

COPY --from=build /app/target/web-*.jar ./web.jar

EXPOSE 8080

CMD ["java", "-jar", "./web.jar"]
