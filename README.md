# ♕ BYU CS 240 Chess

[[Sequence Diagram]](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkwBzKBACu2GAGI0wKgE8YAJRRakEsFEFIIaYwHcAFkjAdEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAej0VKAAdNABvLMpTAFsUABoYXCl3aBlKlBLgJAQAX0wKcNgQsLZxKKhbe18oAAoiqFKKquUJWqh6mEbmhABKDtZ2GB6BIVFxKSitFDAAVWzx7Kn13ZExSQlt0PUosgBRABk3uCSYCamYAAzXQlP7ZTC3fYPbY9Tp9FBRNB6BAIDbULY7eRQw4wECDQQoc6US7FYBlSrVOZ1G5Y+5SJ5qBRRACSADl3lZfv8ydNKfNFssWjA2Ul4mDaHCMaFIXSJFE8SgCcI9GBPCTJjyaXtZQyXsL2W9OeKNeSYMAVZ4khAANbofWis0WiG0g6PQKwzb9R2qq22tBo+Ew0JwyLey029AByhB+DIdBgKIAJgADMnrChsK4VHl8uafRG0O10DJNDp9IYjNBeMcYB8IHY3EYvD4-HGgsHWKG4olUhkVFIXGgc9zTXy6u0Q90O+ivQh60g0OqphTZvz1pLxAyZa6onnPPHCASiWMR7zV9TndrXbqmeRPt8uVceSuanUYAAxKzxACyxsvd2vd1p3hKJHD0DgN1UTErweeV8V8ZVVSXTV-2xN1nlvNkOUfUlTT3X07RFMU91QnUgN6dhdwtAj-UgmNJyiU9Knwgso26Hp-HjJNU3TCRMzQbM3EKJ88OogsizQEttF0AxjB0FA7TrLR9GYJtvF8TBOPbXouz4e8kjeNJ0n7CRBzyFi-TYhlIKiOdlJVUYLPQddPSg4Jt1gtQUAQE4UEQtUnLQLUAOhHo9T0r4DLDGiPy-X8SI8+lyJsmAkRRAMMQ9LpEWRVFJyDLSExgFM0xsPisw4YtSxkitBhkWthhgABxHlHjUltNLbZgspoKAomiJq3l7dItB5cyxMs-KQhS5AHBasoJEcibnIyzdoJCnFjjAfylvzP1grQm9XnvH5jQBT8fzDGA8gAdQACUNN5ooLGAAF4YFzZbC1IwCesos6eVWlAQkSuUYFmsB5skZCygO2UQj1d4vlO0aymZPhKmbXxj2Xa60AUBBQGtHHnxgVGUFZHlYsu8m8fux6YCxwkRNUd7hNwlBWhgeIrDxgmiZJso3o+092lBoCUuifJyfRzH1OZjnKjyfmQGJlnKnJymylacoADp9ZSIGJc7PqYmlnlZcZ+XBemZXCdVm2NZ5LXOb1g22KAwruNKyR+ME-IACJyYkAP+vNtGMatltHb5+21cVsnnZ5HX9d1lIJKkstZKMbA9CgbAfPgeDVChjx5c6gJuuArsEmSIzyfGvb0BzTXk494IUoVAkod28M-SdsoXZcmc3JgUGjhOHbArhwCMOO5GcJNVQLvii06YeqwnsC4XPqb77Qfo1zGJZo33JdTyu98HuZcjpmY7tgX1cTweUPF4I9Sww1fhvuXo6fh+HZP1bkLIiz8ODi2mkfMB6MjaQOytAvg7cvbFR4mVP2KAW4Wz4Bnaq5ZjDmG8nOdwMAABSEAFzNSpkYFW1oK7xmsibfqsRTjDQbqYL6OZCpwAgHOKAA8UDownCbOBIEYAACtyFoB7oFSoIAurcN4fw9Gw9AzrTQhPbaFpe40RnqFOed4F4AyFivK6t0N5by+jvQKYtz5JT+l6U8p9x5kxONfLBuipAI1vEjB8CDf7Y3-vjOOMdgHLzimA9eDMb47xvjYmCnj363lOLEPgwgoq0zyO8b+WD-EKyXkrIJj8E6hMiZvBBMSsFxI2hIY2I8crpUgrU3q9S8rCOCMgkqvF0GYCqtJPBRgdDAEsIgRUsBgDYALoeAgrgy4dUKgw+B0QIpDUMhkdQ7dO4+TwKMFR7AQa2LBnI0ZOyfoPC8VEZZUUABU1NfwTFOQk8K+kno3JMQlA55yx7PJgK88J5MHk1JEf9NKrSR5NNDCCj2IQOmoN9hVTQVUgA)

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

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
| `mvn -pl shared test`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
