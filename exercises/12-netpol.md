## Exercise 12: Network Policies

### Goal:
Securing network connections of the voting app

**Steps:**

* Allows only the network routes shown in the diagram
* Prohibit outgoing traffic from the cluster
* Omit ports for now, those who have time will add them later.


### Examples
**Deny all**
```
kind: NetworkPolicy
apiVersion: networking.k8s.io/v1
metadata:
  name: ingress-deny-all
spec:
  podSelector: {}
  ingress: []
```
**Ingress external**
```
kind: NetworkPolicy
apiVersion: networking.k8s.io/v1
metadata:
  name: ingress-allow-vote
spec:
  podSelector:
    matchLabels:
      app: vote
  ingress:
    - ports:
        - port: http
      from: []
```
**Ingress other Pod**
```
kind: NetworkPolicy
apiVersion: networking.k8s.io/v1
metadata:
  name: ingress-allow-podA-to-access-podB
spec:
  podSelector:
    matchLabels:
      app: podB
  ingress:
  - ports:
      - port: 1234
    from:
        - podSelector:
            matchLabels:
              app: podA
```



### Tips
<!-- .slide: class="hideForTrainingKind-ckad"  -->
```bash
# Show labels
kubectl get pods --show-labels
# Restart after DENY ALL (e.g. worker sets watch to REDIS -> connection open)
kubectl delete pod --all
# Helpful for debugging
kubectl logs <podname>
# Test if connection to the outside is still possible?
kubectl exec $(kubectl get pods  | awk '/^result/ {print $1;exit}') wget google.de
```

