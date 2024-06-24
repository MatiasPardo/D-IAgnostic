FROM maven:3.8-openjdk-17-slim as builder

WORKDIR /bot
COPY . .
RUN mvn package -DskipTests

FROM eclipse-temurin:17-jre-alpine

COPY --from=builder /bot /app
WORKDIR /app
#EXPOSE 8080
CMD ["java", "-jar", "target/TACS-BOT-1.0-SNAPSHOT-jar-with-dependencies.jar"]
