## Exercise 10: SecurityContext

### Goal:

Securing Vote and Results Deployments

**Example for a Pod**
```
apiVersion: v1
kind: Pod
...
metadata:
  name: pod-name
spec:
  securityContext:
    seccompProfile:
      type: RuntimeDefault
  enableServiceLinks: false
  containers:
  - name: restricted
    securityContext:
      runAsNonRoot: true
      runAsUser: 100000
      runAsGroup: 100000
      readOnlyRootFilesystem: true
      allowPrivilegeEscalation: false
      capabilities:
        drop:
          - ALL
```
**Steps:**

Try the settings one by one to identify if there are changes necessary
* Start Container 
  * with read-only file system,
  * with minimal capabilities,
  * without the possibility of privilege escalation,
  * with Seccomp profile activated,
  * as unprivilised user and unprivilised group,
  * without service information in environment and
* If you still have time, the following images are deployed with the same settings
 * `nginxinc/nginx-unprivileged:1.17.2`
 * `springcommunity/spring-framework-petclinic:5.1.5`



### Tips
<!-- .slide: class="hideForTrainingKind-ckad"  -->
* At the first start errors will occur!
* Vote
  * Image must be adjusted: see last line 'Dockerfile'.  
    Do not forget `docker push`!
  * You have to give the application in K8s write access to a certain 
    Give folder.
* Result: No new image necessary! Port can be changed by Env Var 'PORT
* Vote & Result: On port change - Adjust 'targetPort' in the service 
* For the deployment of NGINX and Petclinic:
  * NGINX unprivileged and Petclinic both listen on port 8080
  * Let YAML generate a structure and then edit it. e.g.
* In Result Readiness and Liveness Probes have to be adjusted

```bash
kubectl create deployment nginx --image nginxinc/nginx-unprivileged:1.17.2 --dry-run -o yaml > nginx.yaml 
```

### <i class='fas fa-thumbtack'></i> SecurityContext Tips

```bash
# Query which capabilities are active in the container:
capsh --decode="$(kubectl exec <pod> cat /proc/1/status | grep CapBnd| cut -d':' -f2)"

# Check whether seccomp is active: "2" means active.
kubectl exec <pod> cat /proc/1/status | grep Seccomp

# Check if privilege escalation is possible
kubectl exec <pod> cat /proc/1/status | grep NoNewPrivs

# Simple test to write to a read-only root filesystem
kubectl exec <pod> touch a
# touch: cannot touch 'a': Read-only file system

# Query environment of a container
kubectl exec <pod> env

# Query user of a container
kubectl exec <pod> id
```