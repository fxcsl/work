#!/bin/bash
kubectl delete configmap web
kubectl create configmap web --from-file=config
