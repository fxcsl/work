#!/bin/bash
kubectl apply -f gateway.yaml --record

#update version
#kubectl set image deployment gateway gateway=10.45.136.151:1180/middle/gateway:1.0.1