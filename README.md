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

[//]: # (https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5kvDrco67F8H5LCBALnAWspqig5TTuYhB6rC+6Hqi6KxNiCaGC6YZumSFIGrS5ajiaRLhhaHIwNyvIGoKwowK+YiutKSaXrB5R0dojrOn+UEliUSAAGaWFU+ivEsxECto77iZ+2L-r+RQcTAnrekGAZBiG2EanhLLlLE0AoLG-rTiGLHmpGVHRjAJnxnKOHJvxaaoTy2a5pgimdj+VwDCRowLpOfTTrOzbjq2fTfleIKnNkPYwP2g69H5I4BeF1ZTkGoXzulbbLqu3h+IEXgoOge4Hr4zDHukmSYHFF7KSU5QVNIACiu6tfUrXNC0D6qE+3QhY26Dtkpim+UNc4QZ5zlsth5QIBVvqaXWw1oFhDm6eR+EwOSYAmStM5rWRTJupR5Q0TGWnaEKYSTSNFkRjBTryrZ13yDxiYEnppJGCg3CZAd93raG22sYUlrpsMEA0G9cbcTpbFnECJaoZV7kIHms1Iz5CUDkOfSjOowDkuMUyoeStqVTAObhkJSoIJYkWjZ29VgH2+PJUTqgk-c5MHpTMDU7Tbr08qTNLpwBXroEkK2ru0IwAA4qOrLVaedXnswz3XkrnU9fYo6DVla0s05KNpv0wPTV5z3gjAyCxCrRModCztqOhGIbbBW2nfpu0UkDJtzidZoRhDVGXXDwY3QxwOg37ll269dkI5tuFg79e0KMq7uqLCocUVZ5RKxStnKsrqsJ2H3kOeUCuxELQmV6M9k+0j417m7qsY1gMDI6mtfUCWiUE4bRNkxU-TjygACS0hkwAjL2ADMAAsTwnpkBoVgqDNPDoCCgA2O-AXvypPDPAByZ9TO7MCL5FswuC-MCNAc-f9z+rNaxzSXT6rSeADRjzyXqvDeUwt76n8nzRUF8piH2PqfAK58ECX1HDfFBd9RwPyfjAF+Lg36SxXJ4QqG5sA+CgNgbg8BdSZBboYdWtU2ZzWis1WoDQDZG2CMHdAQ5r6jiisPMazkJq8PnJ8dBoxMH3EXDNC2rCVJqUyO7WEcA6EoHdp7TCn0cLfUzu6AO+0NLA0LmdYu1EeRXXhvIW6MB46PSHi9OC0c27ON9mHcoyjNGjlhDPcyP1w7snKDZd2bjnTm1TOUdRXoVGjl7vIweOM2F43-n0GeoCJzM2-kjNmf8x6jkyRFYh0sioBEsP9RayQYAACkIA8gYYERBIAGyaxyNrdiTVKhVEpHeFoM9jarTnEOahwAKlQDgBARaUBZgZOkEI44kTgRiKGc2LJTxRnjMmdMsmGV0mFIXlkyCCjk4uIAFb1LQKoupPItEoDRF7XRHjwwEWMX6Q62UzHg2CZY3kqdbFx3EdXIupyQnvWAE8jOidDF7VUXMr5lkI4XSsQw+iYQ5nAvMaC1Fad276OheUPwWg4mjD8Qc98R8tlTOgLMJiCKgmQ0pNgYlhgwk3WSBkVIMA0AoGqZs2gjjkkqTID4cMkKlkj05j0Y5SSdZplHr0BZHZcm-1SUOEppCZYBC8GMrsXpYDAGwNQpCBBEgpBqmedpiiuktXap1bqrRjBmz4hbK4MroKdPtiAbgeAFBGuQCAeIiQC6QvxZ41SPqoAGnzvS86Mh-oUkMMACuXEPqIzDS8iN+qTIxsxd8yG0gE30OTQgVxuL3FQvDd6-VecQ2CqRfGgGSaK5srTenCV8qpXuuVXK-JiqzaxVVQq6VZgpZAA)
