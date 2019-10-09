#!/bin/bash
kubectl apply -f service.yaml --record

#update version
#kubectl set image deployment service service=10.45.136.151:1180/middle/service:1.0.1