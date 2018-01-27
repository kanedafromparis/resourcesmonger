FROM openjdk:8-jre-alpine3.7

LABEL artifactId=resourcesmonger version=0.1-SNAPSHOT

ENV JVM_OPTIONS="-Djava.security.egd=file:/dev/./urandom \ 
                                        -XshowSettings:vm"


ADD ./target/resourcesmonger-0.1-SNAPSHOT-fat.jar /opt/resourcesmonger/resourcesmonger.jar

RUN sh -c 'touch /opt/resourcesmonger/resourcesmonger.jar'
EXPOSE 8080

WORKDIR /opt/resourcesmonger

ENTRYPOINT java $JVM_OPTIONS -jar resourcesmonger.jar


