## sbt project compiled with Scala3

### Usage

This is a simple sbt project built using Scala3. You can compile code with `sbt compile` ,run it
with `sbt run` and execute test cases with `sbt test`. The command `sbt console` will start a Dotty REPL.

### About the Project
The project is based on akka-typed actors. There are mainly 3 actors in this project
- PingActor
- PongActor
- PingPongCounterActor

PingActor and PongActor sends messages to each other. PingActor sends Pong message to PongActor and PongActor when receives this message, sends back Ping message to PingActor.

The PingPongCounterActor, this actor holds the number of times Ping and Pong Actor sends message to each other. Both the Ping and Pong actor also send the same message to PingPongCounterActor. The PingPongCounterActor increments the pingCounter and pongCounter by 1 each time it receives the corresponding message.

After a few seconds(5 second) of application start, PingPongCounter actor sends message to both the Ping and Pong actor to stop themselves.

### Scala3 Enums
The project uses enums to create ADTs (or let's call EDTs) to build message type an actor can have.
It also uses UnionTypes which can be of more than one type