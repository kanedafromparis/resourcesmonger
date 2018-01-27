= ressouresmonger

This is a sample application used for my kubernetes introduction course

== Purpose

The purpose of this application is to show that jvm (1.8) does does take docker (cgroups) limits in its usage of ressources.

This is a simple sever with :

````
curl $(minikube service ressourcesmonger --url)/api/0.0.1/infos/env | jq .
````

to displays it's env variables

````
curl $(minikube service ressourcesmonger --url)/api/0.0.1/kaboom/ram | jq .
````

to make it consume RAM to the Exception java.lang.OutOfMemoryError: Java heap space, that should conduct to a crash and respawn of a pod.


```
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
 ...
spec:
  ...
  template:
    ...
    spec:
      containers:
      - env:
        - name: JVM_OPTIONS
          value: "-Djava.security.egd=file:/dev/./urandom -Xmx1g -XshowSettings:vm"
        image: kanedafromparis/ressourcesmonger:0.1
        imagePullPolicy: IfNotPresent
        name: ressourcesmonger
        resources:
          limits:
            memory: 256Mi
...
````

```
curl $(minikube service ressourcesmonger --url)/api/0.0.1/kaboom/ram | jq . 
```

the pod is killed before java.lang.OutOfMemoryError.

== Usage

Build

```
mvn package && \
  docker build -t $USER/ressourcesmonger:0.1 . -f src/main/docker/Dockerfile && \
  kubectl run ressourcesmonger --image $USER/ressourcesmonger:0.1 --port 8080 && \
  kubectl expose deployment ressourcesmonger --type=NodePort
```

Check the logs

```
kubectl logs -f $(kubectl get pod -l run=ressourcesmonger --no-headers | cut -d " " -f 1)
```

clean 

```
kubectl delete -l run=ressourcesmonger
```

=== Todo
 - Looka at the Sprind and Vertx version (maybe)
 - used the fabric8 plugin (maybe)