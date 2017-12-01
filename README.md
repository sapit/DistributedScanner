# Distributed Scanner
This is a repository for our Distributed Systems coursework project. Our aim is to build a distributed vulnerability scanner, which attacks the given website concurrently. 
For the purpose we are building a [vulnerable web app](https://github.com/sapit/vulnerable_web_app/tree/master) as well.

This is setup using Java 8 and Maven as a build tool

## Instalation

### IntelliJ
Open the project by clicking on the `pom.xml` and 'Open as project'.

### Maven Compilation
The following command should take care of dependencies and compilation
```
mvn clean install
```
Produces 4 .jar files for the four roles we have - queue, server, worker, client.

### Roles
The project allows you to assume 4 roles in the form of jars, generated from maven - application, queue, server, client. If you run the 'start.sh' script with no parameters, you will see what parameters you can use.
* __Queue__ is independent
* The __Server__ and and the __Worker__ depend on a running __Queue__
* The __Client__ depends on a running __Server__


## Run the project
Running the script with no parameters will show a list of commands you can use.
```
./start.sh
```

If host and port are not specified they will take the default values - localhost and default RMI registry port.

### Start a queue:
```
./start.sh -q
```

### Start a worker(application) with a remote queue:
```
./start.sh -w -h 130.209.XXX.XXX  #host of the queue
```

### Start a server with a remote queue:
```
./start.sh -s -h 130.209.XXX.XXX  #host of the queue
```

### Start a client with a remote server:
```
./start.sh -c -h 130.209.XXX.XXX  #host of the server
```
