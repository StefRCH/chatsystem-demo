## Purpose

This repository implements a very simple feature for a chat-system, namely maintaining a contact list with the name of all users that have contacted us.
It mostly aims at demonstrating some of the things that one should find in a typical java project:

- **build configuration**: this project can be run and imported based only on the `pom.xml` file
- **code organization**: code is organized in loosely coupled packages. For instance, the packages `chatsystem.network` and `chatsystem.users` are completely independent of each other and only combined in the main class.
- **thread safety**: access to shared resources (e.g. the contact list) is protected by mutex (with the java `synchronized` keyword)
- **tests**: uses `junit` for testing both the network code, the user list code and their combined usage.
- **observer design pattern**: implemented by the `UDPServer` class to make it adaptable to different contexts
- **singleton design pattern**: to ensure that there is a unique instance of the `ContactList` and that it is usable accross the appliaction
- **logging**: usage of the log4j library (the configuration provided simply prints to the console)
- **error handling**: custom error types, and explicit handling of unrecoverable error (error raised higher the chain of responsibility) or recoverable error (handled locally and continue processing) 
- **continuous integration**: each time the code is pushed to github, the code is compiled and tested (and a notification is sent if that fails)
- consistent **coding style**: the code here (loosely) adheres to the [Java Coding Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html) that are used by virtually all java programmers.


## Usage

Building the program from source requires `maven` to be installed.

```sh
# compile 
mvn compile
# run tests
mvn test
# Run main program (which will simply wait for connection messages)
mvn exec:java -Dexec.mainClass="chatsystem.Main" 
```
In another terminal (linux) you can simulate connection messages:
```sh
echo -n "alice" > /dev/udp/127.0.0.1/1789
echo -n "bob" > /dev/udp/127.0.0.1/1789
echo -n "chloe" > /dev/udp/127.0.0.1/1789
echo -n "alice" > /dev/udp/127.0.0.1/1789
```