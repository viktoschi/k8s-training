## Exercise 5: Persistence

The complete application is running and scalable. We also introduced a database as Deployment. At the moment there is no persistance.
In case of database failure we would lose all our data. 

### Goal:

We want to use volumes to persist data.

### Steps:

#### **1. Persistent Volumes and Stateful Sets**

1. Delete PostgreSQL Pod
2. Create persistant volume claim
  ```
  apiVersion: v1
  kind: PersistentVolumeClaim
  metadata:
    name: postgres-pvc
  spec:
    accessModes:
      - ReadWriteOnce
    resources:
      requests:
        storage: 3Gi
  ```
3. Install persistent volume claim 
  ```
      volumeMounts:
       - mountPath: /var/lib/postgresql/data
         name: postgres-volume
  ......
    volumes:
      - name: postgres-volume
        persistentVolumeClaim:
          claimName: postgres-pvc
  ``` 
   <i class='fas fa-thumbtack'></i> mount path: `/var/lib/postgresql/data`

3. Give votes, delete PostgreSQL Pod and create new one  
   Is the data (in Result App) still there?
4. Delete PostgreSQL Deployment and change to stateful set  
   Notice: Use Persistent Volume Claim *Template* and delete the PersistentVolumeClaim
```yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: postgres
spec:
  serviceName: postgres
  template:
    # ...
    spec:
      containers:
      - name: postgres container
        image: postgres:9.6
        volumeMounts:
        - name: database
          mountPath: /var/lib/postgresql/data/

  volumeClaimTemplates:
    - metadata:
        name: database
      spec:
        accessModes: [ "ReadWriteOnce" ]
        resources:
          requests:
            storage: 1Gi
```

5. Scaling up and down PostgreSQL, looking at Pods.  
   What's problematic about that?S

<i class='fas fa-thumbtack'></i> When restarting PostgreSQL, Result must be restarted, too. Either:
* delete Pod(s)
* or

```bash
kubectl rollout restart deployment result
``` 

