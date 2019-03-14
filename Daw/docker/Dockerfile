FROM openjdk:13-ea-9-jdk-alpine3.9

COPY ./target/daw-0.0.1-SNAPSHOT.jar /target/daw-0.0.1-SNAPSHOT.jar
COPY syncWithDB.sh /syncWithDB.sh

EXPOSE 8443

CMD ["/syncWithDB.sh"]
