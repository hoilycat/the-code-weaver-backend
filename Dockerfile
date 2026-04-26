# 1. Build Stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 2. Run Stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# 3. Environment variables (Render에서 설정할 예정)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
