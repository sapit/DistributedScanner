# Distributed Scanner
This is a repository for our Distributed Systems coursework project. Our aim is to build a distributed vulnerability scanner, which attacks the given website concurrently. 
For the purpose we are building a [vulnerable web app](https://github.com/sapit/vulnerable_web_app/tree/master) as well.

This is setup using Java 8 and Maven as a build tool

## Instalation

### IntelliJ
Open the project by clicking on the `pom.xml` and 'Open as project'.

### Maven
The following command should take care of the dependencies
```
mvn clean package
```

At the current stage of the project just compile and run the UrlAttacker class, which has a `main` method
