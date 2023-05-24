#!/bin/bash

kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.4/aio/deploy/recommended.yaml

cat > serv-account.yaml << EOF1
apiVersion: v1
kind: ServiceAccount
metadata:
  name: admin-user
  namespace: kubernetes-dashboard
EOF1

kubectl apply -f serv-account.yaml

cat > role-account.yaml << EOF2
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: admin-user
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- kind: ServiceAccount
  name: admin-user
  namespace: kubernetes-dashboard
EOF2

kubectl apply -f role-account.yaml

echo "TOKEN: "
kubectl -n kubernetes-dashboard describe secret $(kubectl -n kubernetes-dashboard get secret | grep admin-user | awk '{print $1}')

kubectl proxy > /dev/null 2> /dev/null &
sleep 5

echo " "
echo "Call this URL in browser:"
echo ' http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/'
echo '                                                                           ^                             '
echo 'Maybe you will have to delete this letter to make the URL work ------------|'
