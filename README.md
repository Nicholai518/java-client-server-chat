# java-client-server-chat

This project implements a socket server in Java that allows multiple clients to communicate using a CLI.

## Building Client Artifacts

The client module is setup to build a self container executable jar file.

```
mvn clean compile assembly:single
```

You can then run the client using:

```
java -jar client-1.0-SNAPSHOT-jar-with-dependencies.jar

```

## Features
- Multi threaded application allowing multiple clients.

## Motivation

This project was implemented to prepare for Computer Communications class.
