package com.knoldus.actor

import com.knoldus._
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.receptionist.ServiceKey
import akka.actor.typed.receptionist.Receptionist

object PongActor {

  val pongServiceKey = ServiceKey[PongCommand]("pongActor")

  def apply(counterActor: ActorRef[PingPongCommand]): Behavior[PongCommand] = Behaviors.setup { ctx =>
    ctx.system.receptionist ! Receptionist.Register(pongServiceKey, ctx.self)
    import PongCommand._
    Behaviors.receiveMessage {
      case cmd: Pong =>
        ctx.log.info("Ponged by {}", cmd.actor)
        cmd.actor ! PingCommand.Ping(ctx.self)
        counterActor ! cmd
        Behaviors.same
      case StopPonging =>
        ctx.log.info("Stopping Pong Actor")
        Behaviors.stopped
    }
  }

}

