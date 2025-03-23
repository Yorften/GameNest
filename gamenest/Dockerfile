FROM openjdk:17-alpine

WORKDIR /gamenest

COPY target/gamenest-*.jar /gamenest/gamenest.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "/gamenest/gamenest.jar"]
