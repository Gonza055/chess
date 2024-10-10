# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

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

## Sequence Diagram

[SequenceDiagram Presentation](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=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-fDDN9QvTDCSpFtyCAc0wGi-OItOUgtJH-mfcp-m9NdvDOUNSSB7BoCqKeUj9YLXMcoKx6AsxGmdsGTPFilJsDoUMLC5IGRUgqUa7QAdFmnPwc05W9Q1bpnESJSWIrjT5sGq0om36aY73JcOkV2JlK11aSutA6927U2w3ebFQ7toStVsAdgn68V5IJJCZep+zz0lXc-n+-xb2UDBNiM+nKj7T0R1AG6B5hzzvfZLklpBOTj1PoK03H5AMYdXu+9+vbSO4qfIA2gjHfylw43AwTDcXgSNdi9LAYA2AIWEGjUeeFqHEVJsw8zVm7NObGEFgxydym94zZANwPAChGfIBACz6Qyjmm6drUNcoKVuCZHqQgRkCuet6LnmrxevTIgRfeBoeVqmxrSHnuWtAG3q3lUToxxAdONvSxF-FoCdGN1w63acvHGbGR6BymLunpkLq3Po-cz7m7wa47Tft-7yNAPoJA2+sDOMgA)
[SequenceDiagram Editable](https://sequencediagram.org/index.html#initialData=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-fDDN9QvTDCSpFtyCAc0wGi-OItOUgtJH-mfcp-m9NdvDOUNSSB7BoCqKeUj9YLXMcoKx6AsxGmdsGTPFilJsDoUMLC5IGRUgqUa7QAdFmnPwc05W9Q1bpnESJSWIrjT5sGq0om36aY73JcOkV2JlK11aSutA6927U2w3ebFQ7toStVsAdgn68V5IJJCZep+zz0lXc-n+-xb2UDBNiM+nKj7T0R1AG6B5hzzvfZLklpBOTj1PoK03H5AMYdXu+9+vbSO4qfIA2gjHfylw43AwTDcXgSNdi9LAYA2AIWEGjUeeFqHEVJsw8zVm7NObGEFgxydym94zZANwPAChGfIBACz6Qyjmm6drUNcoKVuCZHqQgRkCuet6LnmrxevTIgRfeBoeVqmxrSHnuWtAG3q3lUToxxAdONvSxF-FoCdGN1w63acvHGbGR6BymLunpkLq3Po-cz7m7wa47Tft-7yNAPoJA2+sDOMgA)