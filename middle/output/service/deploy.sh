#!/bin/bash
kubectl apply -f service.yaml --record

#update version
#kubectl set image deployment service service=162.168.2.141:1180/middle/service:1.0.1