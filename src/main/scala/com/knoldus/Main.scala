package com.knoldus

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import com.knoldus.actor.{PingActor, PingPongCounterActor, PongActor}
import com.knoldus.PingCommand._

@main def Main(args: String*): Unit = {

  val guardianBehaviour = Behaviors.setup[Command] { ctx =>
    val counterActor = ctx.spawn(PingPongCounterActor.apply(), "pingPong")
    val pingActor = ctx.spawn(PingActor.apply(counterActor), "ping")
    val pongActor = ctx.spawn(PongActor.apply(counterActor), "pong")

    pingActor ! Ping(pongActor)
    Behaviors.same
  }

  val actorSystem = ActorSystem[Command](guardianBehaviour, "ping-pong-system")
}
