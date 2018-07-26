FROM openjdk:8

COPY ./target/blog-post-1.0-SNAPSHOT.jar .
COPY ./config.yml .

EXPOSE 10000
EXPOSE 10001
CMD java -jar blog-post-1.0-SNAPSHOT.jar