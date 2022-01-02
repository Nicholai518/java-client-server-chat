# java-client-server-chat

## Building Client Artifacts

The client module is setup to build a self container executable jar file.

```
mvn clean compile assembly:single
```

You can then run the client using:

```
java -jar client-1.0-SNAPSHOT-jar-with-dependencies.jar
```