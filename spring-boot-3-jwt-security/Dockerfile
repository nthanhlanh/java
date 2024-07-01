# Sử dụng một image có sẵn từ Maven để xây dựng ứng dụng
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Sử dụng OpenJDK để chạy ứng dụng
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/security-0.0.1-SNAPSHOT.jar security-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "security-0.0.1-SNAPSHOT.jar"]
