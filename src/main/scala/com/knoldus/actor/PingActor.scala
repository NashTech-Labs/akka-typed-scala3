package com.knoldus.actor

import com.knoldus._
import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.receptionist.ServiceKey
import akka.actor.typed.receptionist.Receptionist
import com.knoldus.actor.PingPongCounterActor.StopThis

object PingActor {

  val PingServiceKey = ServiceKey[PingCommand]("pingActor")

  def apply(counterActor: ActorRef[PingPongCommand]): Behavior[PingCommand] = Behaviors.setup { ctx =>
    ctx.system.receptionist ! Receptionist.Register(PingServiceKey, ctx.self)
    import PingCommand._
    Behaviors.receiveMessage {
      case cmd: Ping =>
        ctx.log.info("Pinged by {}", cmd.actor)
        cmd.actor ! PongCommand.Pong(ctx.self)
        counterActor ! cmd
        Behaviors.same
      case StopPinging =>
        ctx.log.info("Stopping Ping action")
        Behaviors.stopped
    }
  }

}
