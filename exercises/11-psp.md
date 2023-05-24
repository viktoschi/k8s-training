## Exercise 11: Pod Security Policies

## Note: 
PSPs are deprecated so this exercise is deprecated as well. But you can still try to get in working in an older cluster.
Updated exercise will come soon.


### Goal:
Setup the PodPolicies to ensure safety clusterwide

#### 🏋 Exercise 12 - Introducing Least Privilege PSP (& RBAC)
<!-- .slide: id="exercise12" -->

* The permissive PSP `privileged` should no longer be default!
  * Delete ClusterRoleBinding `podsecuritypolicy:all-serviceaccounts`
  * Restart all Pods
  * Short error analysis: Where are the Pods or why do they not start?
  * Use this Least Privilege PSP:  
    <i class='fab fa-github'></i> https://raw.githubusercontent.com/cloudogu/k8s-security-demos/master/4-pod-security-policies/demo/01-psp-restrictive.yaml
* Rebuild Worker so that the PSP is fulfilled (similar to exercise 13)
* For redis and Postgresql: Write PSP, which is as restrictive as possible but
  * Execution as root and
  * permitted with certain capabilites (see tips).



#### 🏋️ Exercise 12 <i class='fas fa-thumbtack'></i> tips (1)

* Get postgres back into `default` namespace if necessary (after exercise 9)
* Worker: Do not execute container as root
* For redis and postgres 
  * Create the following: PSP, `ServiceAccount`, `Role` and `RoleBinding`    
    See also: 🌐 https://kubernetes.io/docs/concepts/policy/pod-security-policy/#example
  * Service Account must be configured by `serviceAccountName` in the Pod. 
    See also:   
    🌐 https://kubernetes.io/docs/tasks/configure-pod-container/configure-service-account/
  * Postgres needs capabilities: `DAC_OVERRIDE`, `SETGID`, `SETUID`, `CHOWN`, `FOWNER`  
    and must be able to write to `/var/run/`. 
  * Redis needs the capabilities: `SETGID`, `SETUID`
* When Postgres is running again result must be restarted

Note:
Procedure Solution:

* `kubectl describe rs` -> Error creating: Pods "postgres-77d47989c4-" is forbidden: unable to validate against any Pod security policy: []
* Apply new PSP, vote would have to run again after a short waiting period
* Worker: Set SecCtx with 'runAsUser', 'runAsGroup
* Copy Restricted PSP, allowedCapabilities, runAsUser, supplementalGroups, weaken fsGroup
* Create ServiceAccount databases
* Create RoleBinding for user and SA
* enter serviceAccountName and capabilities in Redis and Posgres and restart Pods



#### 🏋️ Exercise 12 <i class='fas fa-thumbtack'></i> tips (2)
```bash
kubectl apply -f <file or URL> 
# Convert Redis to a deployment (easier restarts)
kubectl create deployment redis --image redis:5.0.2 --dry-run -o yaml
# Pods "reboot" with
kubectl delete pod --all
# Tips for problem analysis:
# What does a deployment consist of?
kubectl describe <pod|deploy|rs|sts>
kubectl logs <pod>

# (Not necessary for the task)  
# If you want to restore the unlocked PSP for testing
kubectl create clusterrolebinding podsecuritypolicy:all-serviceaccounts \
        --clusterrole=podsecuritypolicy:privileged --group system:serviceaccounts
```
