FROM java:8
ARG jarFile

WORKDIR /

COPY ${jarFile} /app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]