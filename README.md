# Multiplayer Chess — Java client/server application

BYU CS 240 coursework implementing a terminal chess client, HTTP services, WebSocket gameplay, and MySQL persistence. The repository includes course-provided scaffolding and passoff tests alongside the application implementation; those supplied materials are not presented as original work.

## Engineering evidence

| Area | Where to inspect |
| --- | --- |
| Chess rules and game state | `shared/src/main/java/chess/` |
| HTTP endpoints and request validation | `server/src/main/java/server/Server.java` |
| Service/data-access separation | `server/src/main/java/service/` and `dataaccess/` |
| Real-time game messages | `server/src/main/java/websocket/` and `shared/src/main/java/websocket/` |
| Terminal client and API facade | `client/src/main/java/ui/` |
| Service and persistence tests | `server/src/test/java/service/` and `dataaccess/` |

## Local setup

Requires JDK 21, Maven, and a local MySQL instance for the server/integration suite. Supply your own ignored `server/src/main/resources/db.properties`; the course example lives in `starter-code/4-database/resources/db.properties`. Never commit real credentials.

```sh
mvn -pl shared test
mvn install -DskipTests
# Separate terminals, after configuring a disposable local database:
mvn -pl server exec:java
mvn -pl client exec:java
```

The server and client use localhost:8080. Run database tests only against a disposable test database: cleanup routines delete test records. The course API includes an unauthenticated database-clear endpoint, so this version is a local educational demo, not an internet-facing service. Test coverage and scaffolding are visible in the repository; no current CI pass or production-security claim is implied.

[Portfolio](https://gonza055.github.io/)

---

## Original course architecture and development notes

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
