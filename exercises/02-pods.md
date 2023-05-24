## Exercise 2: Pods

In the last exercise we learnded how to run a container locally. Now we want to run containers inside a Kubernetes cluster. We can achieve this by using imperative commands or  with a declarative approach. Also we will setup the Redis Pod to be used by the vote Pod to store votes.

### Goal:

Run the vote and Redis Pod. 
Establish a connection between vote and Redis. 
Make use of yaml files.

### Steps:

#### **1. Tag and push the container**
Like in docker exercise 1:

* Tag the 'vote' image from exercise 1 as
  ```bash
  docker tag vote gcr.io/cloudogu-trainings/GROUPNAME/vote:0.1
  ```  
* Push this image to the registry 
  ```bash
  docker push gcr.io/cloudogu-trainings/GROUPNAME/vote:0.1
  ```

#### **2. Starting vote in a Pod**

* Run a Pod with the image vote:
  ```bash
  kubectl run vote --image=gcr.io/cloudogu-trainings/GROUPNAME/vote:0.1
  ```
* Check the status of your Pods:
  ```bash
  kubectl get pods
  ```

#### **3. Deploy Redis**

1. Understand `k8s/redis.yaml`
2. Deploy it to the cluster with `kubectl apply -f FILENAME`
3. Forward port: `6379` (blocks terminal!) -> `kubectl port-forward pod/redis *Free port*:*containerport*`
4. `telnet localhost 6379` (from another terminal) and talk to REDIS for fun:
    * `append k8s 'is great'`
    * `get k8s`
    * `QUIT`  

Other REDIS commands:
  - `monitor`
  - `LRANGE votes 0  -1`
  - `KEYS '*'`

#### **4. Changes to vote**

1. Activate commented lines in `vote/app.py`
2. Build and push new version of the `vote` image using Docker as before
3. Delete the old Pod `kubectl delete pod NAME`
3. Create Pod for voting app (*`kubectl run ...`* or *`--dry-run=client -o yaml`* or look at the redis.yaml and apply later)
4. Check results so far (remember option `-o wide` for displaying details)
  --> expected result is failure of the Pod

Let's fix that:

1. Stop the `vote` Pod
2. Find out about the Redis IP address by using the option "-o wide" for kubectl get
3. Pass the IP address of the Redis Pod to a new 'vote' Pod by using
  the environment variable 'REDIS_HOST'.
  This can be done on the command line with option  
  "--env=REDIS_HOST=`10.1.2.3`" or by
  adapting a generated YAML file.  

  **Tips:**
* Use `redis.yaml` as example/template.
* ENV Variable in yaml:
  ```
  env:
        - name: NAME
          value: "VALUE"
  ```
* `port-forward` connect to redis. `telnet` - Redis command 'monitor' shows changes
* Use `kubectl` and get info about your Pods.

Note:
* Redis command to display all votes: `LRANGE votes 0 -1`


#### **5. Test application**

* Use port-forward to access vote in your browser
* Call application in the browser and cast a few votes
* Are votes coming in at the Redis? ➡ Testing via Telnet

#### **6. Optional task**
You might be wondering, what happens when the Redis port fails. It most likely gets a new ip address. So let's test that behaviour

1. What IP address does the Redis Pod have? 
2. Delete Redis Pod
3. Run a new Redis Pod
4. What IP address does the Redis Pod have now?
5. Check: Are votes still reaching Redis?
