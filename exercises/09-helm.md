## Exercise 9: Helm (&Wordpress)

Helm is kind of package manager for your kubernetes configuration files. 

### Goal:
Install Helm and use it to quickly deploy a wordpress stack. 

**Steps**:

* Install the Helm client on your VM: `sudo snap install helm --classic`
* On windows you can use chocolatey or the binary from github: https://get.helm.sh/helm-v3.8.0-windows-amd64.zip
* Install the kubernetes wordpress stack 

```bash
# Add the repo
$ helm repo add bitnami https://charts.bitnami.com/bitnami
$ helm repo update
``` 
* Install the chart

```bash
$ helm install wordpress bitnami/wordpress
```

* Wait for the LoadBalancer IP and check it in your browser
* Feel free to customize the chart with values:
  
```bash
helm install my-release \
--set wordpressUsername=admin \
--set wordpressPassword=password \
--set mariadb.auth.rootPassword=secretpassword \
  bitnami/wordpress
```
* Or create a yaml config file <font size="1"> Tip: You can use the default values.yaml <i class='fab fa-github'></i>https://github.com/bitnami/charts/blob/master/bitnami/wordpress/values.yaml</font>
  
```bash
helm install my-release -f values.yaml bitnami/wordpress
```
```yaml
wordpressUsername: user

wordpressPassword: ""
```