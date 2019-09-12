
**update version code**
```
mvn versions:set -DnewVersion=1.0.1

```

**package**
```
mvn clean package -Pprod

```

##update
```$xslt
kubectl set image deployment gateway gateway=162.162.2.141:1180/middle/gateway:1.0.1 --record=true

kubectl rollout history deployment gateway

kubectl rollout undo deployment gateway --to-revision=1
```
