# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

[[Sequence Diagram - Phase 2]](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdADZM9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYHSQ4AAaz5HRgyQyqRgotGMGACClHDCKAAHtCNIziSyTqDcSpyvyoIycSIVKbCkdLjAFJqUMBtfUZegAKK6lTYAiJW3HXKnbLmcoAFicAGZuv1RupgOTxlMfVBvGUVR07uq3R6wvJpeg+gd0BxMEbmeoHUU7ShymgfAgECG8adqyTVKUQFLMlbaR1GQztMba6djKUFBwOHKBdp2-bO2Oaz2++7MgofGBUrDgDvUiOq6vu2ypzO59vdzawcvToCLtmEdDkWoW1hH8C607s9dc2KYwwOUqxPAeu71BAJZoJMIH7CGlB1hGGDlAATE4TgJgMAGjLBUyPFM4GpJB0F4as5acKYXi+P4ATQOw5IwAAMhA0RJAEaQZGImDIWABS-qU1R1E0rQGOoCRoAmqooLMLxvBwBy-gUX6XNJOaUAAjLJ+ivEsMAvJgKlQPx5qNggrE8rCLFsai6KxNid4oAUXYsqU5JUjSNwaaORJrgUU5kD6TE+nA9QwAAVDAnLhDUACy6lQKyADqLA+uEPoJXmAC8UxeX0piOQAPLkLmkjcNpLk5+SOaUVAakgWiZFUOnyTAQ6UI1wBPC8fTYpK7qygAkmgdXIHOcCQhw5hIBqGiGecwLFTVw2jQ1E0oFNGAzQgfoBkGaDzUClAmdQlwxhpWFJqoKb3OmmbQOUK31eNk3TbNZY8cg5hLcUZ1OBdvR9FdN1pn0GZZo9I3PfAr1be9FGVgVpnOaernrXIKDXsqsL9Fadyg3lTyQKRIEaahsaTHsPlMt2-kcoNABygXhGFjP1DU6qHm10mzMT6CYjAABqChMZSPpkG12GaV8ZMUw5pnFaVPZkD4a6HU+P0NqU5k2e+CDMDjfRESRpak+TZby6dUDFUZpToZhgPG1BptTLLH28ZrJR2xhWFOyTrvmwjVHeH4gReCg6DMaxvjMBx6SZJ9OTMGaVuCdIQU+vUPrNC0YmqBJ3R+5H5Sywhxn5LbRdJCX5Pq8CJ1UOCOsx7CfRu5bjcqCjvndm5FJY637cnj3LJ06UgXBaFEVRTF8VETAKVpRlfNJDlbfmxKUpDVDY0wCbB1FSVqOks2rZ15QnvgpSwSHtASAAF4bbtKCBhJZc2wtf0A4mLIgyB4MPT5DfXcd9H7LAOB7XIAlzqXV-qmf+91szXyIqAjaH0KyVUVsfHsRFGStyrmRN2TxpJkTyh3UQ3caauSmtwLch5B7m2puOVQY907BSzjPOKnNdwL1SulGAK8YBrzdpgo+I9SQqzVkZS+Fpm47kiKoD879ci23trA5MqZgJTGsjHGAii1wADMWzuy+hgT2lw1GA2BpokCOidx6NVt2IxOwDhmEop4EOtFJrMWhDAAA4oBVkccuKJ3MGyASFQ-GZxzvYQChdDz7y0aXJSFdP5lCrkk2uRkG5N2hAEpMDC5abwGjAJ6u997n2tlA0ypRT4IG6Mg2+UAH5P39C-fayjbYwKsXA26YNEGWmAakVB4DQlmOqanbpP8NF9IAUgoZIz0GUUcpQ5hfcwCugQPktQsImF+XyAFDOU9IrRS4bEpMoilalB6OclAjItIwFuYyVCAI0kyMbLcpi0JYQVH6Lcwa0g8IPL6LchmgEyK3I0mWaYfzAIArwqhJ4oLwVaJBYBVCZZGgdwviotJ3sHa-LRaMeFSSkWATBbhVFkLoWwuJYCrRiKpjIspSBW5GK9iNDGWAcxZRLGEv+fSkuZLRgUqAqywCUK9gwqJSgElaFhUoFFRC9FmK3GI2oqHAI2AfBQGwNwGGm5DDbJSPHLIpjk6Ol+mUISDQYlxKGYkmu8ZejMsMCBYYlhtkfRSZXBJztq4wFlrMWliqUXus9eC15R1y41Q3BjbZhSvgeq9eQru+QrnuStAAIUsPvRNezaYHI5BPEKYU1KnLnlzRe-DBHCI3v1GUpSd4NT3v6y52DaktjbNIiZndGyNJAc0sBz9X7Bl-B-aNpQplA16aDOZgyUFDrQRA81PKp3-XUddeBd0IZAMXS00ZGDD5XKllAKFlVVlrl7P2FACaQ1KtRcmyNBbR5FtKIzZmrMGbs0eYEtqrqBbC1FuLSWT7cKppQFg8RPYQ1ypgLGKNGte3gjjZkbZCiPyGwFXhBD2KqmqJ9oDbDWiENcrXZY2Dgr4NBw8TRQIlgUCtggMkGAAApCAPJ-GAUCDoBAoBpRcvCVawSVRKQiRaLc+JEF-WZOdf0PVwAGNQDgBAcyUA8IGG4OADaKm1MKrg6Rn1eKMlOuDc8PjSndPQA04xlA2mOBWagPpqjpHsnVRqQAKw42gBN68KazE03ZzIDnVPQFmLGPqW8m2rTnBUntNU6kNIWUujgI6OnjtxZO6d1jZkDL3U0g9Jik5ruy7OhBu6B3DJS0sysKz00dvcgmiLw8qHqDHiW45nD4q3OSnwjKLacqxnbdBtygEbQNtlMamIMAeQxbI8hi0ZSGrbLS2-DLXTCPTK3WKndgCltzluUV76va-oOy2yDVF87ovQ0O0HCbXG1TTecW2SBy1m0cBW200dB8rUTqfOuuTM6ZlkSu8a57R3xnQLjJui75XAFg67TVpGDZL29z8I1W9gFYSxmDYF+zjnUV5Spi15hY9KRVGkAoDhPXyCZz0bZ-HoXYBrzyrwpeM25yDeG615WjiWSVPeaULzPJ0N6ywOtvFFGgfbbIuxnkG1f1qljOqVkkwADU9OtPBccyu4rJ3eWbel7DqYcu0AK9ufBlX+ENd4+10zmjGraJeEU-AbgeB1TYD1YQeIiQTUhN4kJ1OlQ2GZ2zq0YwZdlJ4sqTki0IA3dQE2XgiDqPqG2a3K2K0qhdkk-2VOEPHCK0JQ0HVq5NCKSY1bFjbPL62tvoLxlIvREL31ZG+XjPWzAk56VqwjOhfZ6K7UNztZkjuwC4W42eP7ooAYf1p0yXhuctkUZHoAwMAp94GXfNgSUul+opX-oQwG-Jo1aAA)

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
