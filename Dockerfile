FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
ADD pom.xml .
RUN mvn dependency:go-offline
ADD . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app
COPY --from=build /app/target/application.jar ./
USER 1000:1000
ENTRYPOINT ["java", "-jar", "application.jar"]
