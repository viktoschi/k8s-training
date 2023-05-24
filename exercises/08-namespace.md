## Exercise 8: Namespaces

For some applications it is usefull to split them into Namespaces.

### Goal:
Create a new Namespace and move everything from PostgreSQL into it. 
Update the existing application to work again.

**Steps**:

* Delete the PostgreSQL Pods, services and secret
* Create the Namespace data `kubectl create namespace data` or via yaml
* Deploy the PostgreSQL Pods, services, secret again in the namspace `data`
* Change `worker` and `result` so that the system continues to work. `SERVICE_NAME.NAMESPACE.svc.cluster.local`  