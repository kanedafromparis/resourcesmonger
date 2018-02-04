FROM openjdk:8-jre-alpine3.7

LABEL artifactId=resourcesmonger version=1.0

ENV JVM_OPTIONS="-Djava.security.egd=file:/dev/./urandom \ 
                                        -XshowSettings:vm"


ADD ./target/resourcesmonger-1.0-fat.jar /opt/resourcesmonger/resourcesmonger.jar

RUN sh -c 'touch /opt/resourcesmonger/resourcesmonger.jar'
EXPOSE 8080

WORKDIR /opt/resourcesmonger

ENTRYPOINT java $JVM_OPTIONS -jar resourcesmonger.jar


