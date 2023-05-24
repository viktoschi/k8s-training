## Exercise 7: Resources
To handle the resources efficiently, Kubernetes has the option to request and limit resources for Pods.

### Goal: 
Testing the useage of limits for Pods

**Steps:**

* Open a shell in a `result` Pod and execute the following commands:

  ```bash
  apk update
  apk add bash 
  bash
  # Shell expansion that floats the memory
  echo {1..1000000000}
  ```

* After the Pod is killed, view its description and find the reason for the kill.
* Insert a reasonable memory limit and repeat the test.

  ```
  containers:
    - image: ...
      name: ...
      resources:
        requests:
          cpu: "500m"
          memory: "128Mi"
        limits:
          cpu: "1"
          memory: "256Mi"
   ```
* Open a shell in a `result` Pod and execute the following command:

  ```bash
  while true; do true; done
  ```

* Check the CPU usage of the Pod.
* Insert a CPU limit and repeat the test.
* How is the CPU load now?

#### Query resource usage

* View current resource requests and limits of the Pods on the respective nodes    
  ("Overcommitted": If sum of the limits > the resources of the node)

  ```bash
  kubectl describe node 
  ```

* View actual current resource usage: 

  ```bash
  kubectl top pod
  kubectl top node
  ```