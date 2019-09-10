#!/bin/bash
kubectl apply -f gateway.yaml --record

#update version
#kubectl set image deployment gateway gateway=162.168.2.141:1180/middle/gateway:1.0.1