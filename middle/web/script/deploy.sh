#!/bin/bash
kubectl apply -f web.yaml --record

#update version
#kubectl set image deployment web web=162.168.2.141:1180/middle/web:1.0.1