FROM gcr.io/distroless/java:11

COPY target/mvc-demo-bootable.jar /app/main.jar
WORKDIR /app
EXPOSE 8080

CMD ["main.jar"]
