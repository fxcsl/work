#!/bin/bash
kubectl delete configmap gateway
kubectl create configmap gateway --from-file=config
