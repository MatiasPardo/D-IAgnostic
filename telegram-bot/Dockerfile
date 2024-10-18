FROM openjdk:17

WORKDIR /app

ADD ./target/diagnostic-bot-1.0-SNAPSHOT-jar-with-dependencies.jar target/diagnostic-bot-1.0-SNAPSHOT-jar-with-dependencies.jar

#EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/target/diagnostic-bot-1.0-SNAPSHOT-jar-with-dependencies.jar"]
