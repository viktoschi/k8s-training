## Exercise 4: Deployment

With services running, we are ready to complete the app.

### Goal:

Deploy the rest of the app. 
Creating a Deployment yaml file for vote. 
Test the scaling of Deployments.

### Steps:

#### **1. Complete the app**


* Deploy PostgreSQL including service  (`kubectl apply -f k8s-training/k8s/postgres.yaml`)
* If necessary, update the name of your Redis service   
  in env var `REDIS_HOST` of `worker.yaml`
* Deploy worker (`worker.yaml`)
* Deploy the result app including service (`result.yaml`)


#### **2. Write and scale Deployment**

* Apply Deployment YAML for vote, look at result.yaml or generate a template
  <i class='fas fa-thumbtack'></i> tip: `kubectl create... --dry-run=client -o yaml`
  
* Scaling up and down Vote Deployment `kubectl scale deployment vote --replicas=3`
  * Watching Pods
  * Delete a Pod and check effect
  * Reload several times in the browser - check Pod name
* Scaling up and down worker deployment.
  * Call votes multiple times in the browser.
  * View in browser result app
  * Observe logs of the worker Pod. <i class='fas fa-thumbtack'></i> Follow logs with
```bash
kubectl logs -f podName
```


#### **3. Update Deployment**

With 3 replicas (enough to observe changes):

* Change Vote Deployment so that a non-existent version of the image is used
  * Watch rollout status
  * Watching Pods (`watch`)
  * Query Deployment
  * In the browser: Is the application still available? Which Pods are responding?
* Fix problem with rollback and/or in Deployment YAML
  * Observe rollout status
  * Watching Pods
  * Reload vote several times in the browser - watch Pod name
* <i class='fas fa-code-branch'></i> When you are satisfied, commit your `yaml` files to Git and push

<font color="red">⚠</font> If Pods stop at 'pending' you overprovisioned the cluster. 

* It is easy to fix a failed Deployment with
  `kubectl rollout undo` or better in the YAML
* `kubectl scale` or `replicas`



#### **4. Deployment - optional**

* Experiment with 'maxSurge' and 'maxUnavailable': `0%`, `50%`, `100%`
* Observe zero downtime Deployments:

```bash
EXTERNAL_IP=35.202.172.251	# of your vote service
while [ 1 ]
do
 # Vote a or b randomly
  if [[ $((RANDOM % 2)) -eq 0 ]]; then vote='a'; else vote='b'; fi

  echo $(date '+%Y-%m-%d %H:%M:%S') vote=${vote} $(curl -isS http://${EXTERNAL_IP}/ --data "vote=${vote}" | head -n1)
done
```

Perform a Deployment while the script is running.