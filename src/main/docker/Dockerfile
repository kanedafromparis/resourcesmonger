FROM openjdk:8-jre-alpine3.7

LABEL artifactId=ressourcesmonger version=0.1-SNAPSHOT

ENV JVM_OPTIONS="-Djava.security.egd=file:/dev/./urandom \ 
                                        -XshowSettings:vm"


ADD ./target/ressourcesmonger-0.1-SNAPSHOT-fat.jar /opt/ressourcesmonger/ressourcesmonger.jar

RUN sh -c 'touch /opt/ressourcesmonger/ressourcesmonger.jar'
EXPOSE 8080

WORKDIR /opt/ressourcesmonger

ENTRYPOINT java $JVM_OPTIONS -jar ressourcesmonger.jar


