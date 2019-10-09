#!/bin/bash
kubectl apply -f web.yaml --record

#update version
#kubectl set image deployment web web=10.45.136.151:1180/middle/web:1.0.1