# java-client-server-chat

A CLI application that supports multiple clients to communicate by connecting to a server / port.

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
