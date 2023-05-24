## Exercise 6: Ingress

> An API object that manages external access to the services in a cluster, typically HTTP.
> Ingress can provide load balancing, TLS termination and name-based virtual hosting.

### Goal:
Use Ingresses as single entry point to the cluster. 


#### Example Ingress
<img data-src="images/traefik.png" width=22% class="floatRight"/>

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: vote-ingress
  annotations:
    kubernetes.io/ingress.class: traefik # deprecated
spec:
  ingressClassName: "traefik-lb" # if IngressClass is used and 1.19+
  rules:
  - host: vote.yourdomain.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: vote
            port:
              number: 80
```




#### 🏋 Exercise 7 - Ingress
<!-- .slide: id="exercise7" -->
<font size="1">Notice: We will install the helm client like in the helm exercise. 
For now we just use helm, no need to understand it right now. </font>

* Install the helm client on your VM: `sudo snap install helm --classic`
* On windows you can use chocolatey or the binary from github: https://get.helm.sh/helm-v3.8.0-windows-amd64.zip

https://doc.traefik.io/traefik/getting-started/install-traefik/

* Install Træfik as Ingress Controller (see [Træfik-Docs](https://doc.traefik.io/traefik/getting-started/install-traefik/))
  * helm repo add traefik https://helm.traefik.io/traefik
  * helm repo update
  * helm install traefik traefik/traefik
* Create Ingress objects for `vote` and `result`
  * Change the service to type 'ClusterIP' (delete the service)
* In the `/etc/hosts` create the entry for `vote` and `result`    
(point to the external IP of the Træfik service)  
* Check in the browser whether the correct page is sent



#### 🏋 Exercise 7 - Ingress Bonus

Set Traefik as default IngressClass

* Check your Ingresses with
```bash
$ k get ingress
```
* Create an IngressClass Yaml file
* Delete your Ingresses and apply them again
* Check your Ingresses again


Note:
* Extension (but makes NetPol exercise more difficult!)

