#!/bin/bash
kubectl delete configmap service
kubectl create configmap service --from-file=config
