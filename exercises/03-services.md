## Exercise 3: Services

We got our Redis and vote Pods running. As you may noticed, if the Redis Pod gets a new IP, we have a problem. 

### Goal:

Creating services for the vote and Redis Pod. The vote Pod should also be exposed to the internet.

### Steps:

#### **1. Create Redis Service**

* Edit the redis.yaml and add a label
* Create a Redis service with selector on label from 1.
   Hint:
```
apiVersion: v1
kind: Service
metadata:
  name: 'SERVICE_NAME'
spec:
  selector:
    app: 'LABEL'
  ports:
    - port: 'PORTNUMBER'
```
* Vote app Pod: replace Redis IP address with host name (service name) as environment variable
* Check: Is data arriving at Redis now?
* Expected result: Redis Pod has different IP, but data is still arriving!



#### **2. Make Vote App available on the Internet**

* Vote App Pod: Add label
* Create Vote App Service with selector on label from 1. and 'type: LoadBalancer'
* Wait for External IP (you might want to use '--watch')
* Test the vote app in the browser 
* <i class='fas fa-code-branch'></i> When you are satisfied, commit your `yaml` files to Git and push

Note: 
* **A drawing can be helpful here!**  
  VoteSvc -(sel./label)-> VotePod -(hostname)-> RedisSvc -(sel./label)-> Redis Pod
* sample solution 3: 'kubectl get services --watch