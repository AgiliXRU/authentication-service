FROM java:8

COPY mappings /mappings

#RUN  curl -L "https://github.com/holomekc/wiremock/releases/download/2.25.1-ui/wiremock-standalone-2.25.1.jar" -o app.jar
COPY wiremock-standalone-2.25.1.jar app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]