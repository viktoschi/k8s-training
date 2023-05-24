## Exercise (demonstration example): Kubernetes quickstart

If you want to recreate the steps from the demonstration, just follow the instruction:

### Goal:

Running the vote container and creating a service. 

### Steps:

#### **1. Tag and push the container**
Like in docker exercise 1:

* tag the 'vote' image from exercise 1 as
  ```bash
  docker tag vote gcr.io/cloudogu-trainings/GROUPNAME/vote:0.1
  ```  
* push this image to the registry 
  ```bash
  docker push gcr.io/cloudogu-trainings/GROUPNAME/vote:0.1
  ```



#### **2. Starting the Container in a Pod**

* Run a pod with the image vote:
  ```bash
  kubectl run vote --image=gcr.io/cloudogu-trainings/GROUPNAME/vote:0.1
  ```
* Check the status of your pods:
  ```bash
  kubectl get pods
  ```


#### **3. Enable access via the Internet**

* Create a service (expose the pod) 
  ```bash
  kubectl expose pod vote --port 80 --type LoadBalancer
  ```

* Check the ip (it may take some time)
  ```bash
  watch kubectl get services
  ```


#### **4. Cleaning up**

* Delete the pod vote
  ```bash
  kubectl delete pod vote
  ```

* Delete service vote
  ```bash
  kubectl delete service vote
  ```

