## Exercise 1: Creating a Docker Container

### Goal:

We want to make sure the basic Docker commands are familiar. So let's build the image for the vote application and run it locally.

### Steps:
1. Build the image
```bash
docker build -t vote .
```

2. Run the container
```bash
docker run -p 8080:80 vote
```
### Usefull commands for interacting and debugging:

* Opening a terminal in container (**i**nteractive **t**ty):  
  (technical: start another process called "sh" in the namespace of the container)

```bash
docker exec -it vote-container sh
``` 
* Display logs of the container:

```bash
docker logs vote-container
```
* Display running containers (**p**rocess **s**tatus):

```bash
docker ps
# or 
docker container ls
```
* Push the Docker Image into a registry 

```bash
# default registry (Docker Hub)
docker push vote:0.1
# another registry
docker push other.registry.url/vote:0.1
```

* Stop container
```bash
docker stop <containerNameOrId>
``` 
* Show stopped containers with
```bash
docker ps -a
``` 
* Stopped containers can be started again with
```bash
docker start <containerNameOrId>
``` 
* Delete container
```bash
# Stop and delete (also no longer visible with "ps -a") 
docker rm -f <containerNameOrId>
``` 

* Create tag for existing image
```bash
docker tag vote-image:0.1 gcr.io/cloudogu-trainings/GROUPNAME/vote:0.1
```
* Copy files into running container (also works vice versa, like 'scp')
```bash
docker cp ~/file.txt vote-container:/path/in/container
```

<i class='fas fa-thumbtack'></i> For `<containerNameOrId>` the first characters of the ID are sufficient for identification