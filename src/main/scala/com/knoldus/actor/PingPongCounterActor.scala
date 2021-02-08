package com.knoldus.actor

import com.knoldus._
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.receptionist.ServiceKey
import akka.actor.typed.receptionist.Receptionist

import scala.concurrent.duration._

object PingPongCounterActor {

  import PingCommand._
  import PongCommand._
  import LookupCommand._

  case object StopThis

  val pingPongServiceKey = ServiceKey[PingPongCommand | StopThis.type]("pingPong")

  private var pingCounter: Long = 0
  private var pongCounter: Long = 0

  def apply(): Behavior[PingPongCommand | StopThis.type] = Behaviors.setup { ctx =>
    ctx.system.receptionist ! Receptionist.register(pingPongServiceKey, ctx.self)

    Behaviors.withTimers[PingPongCommand | StopThis.type] { timers =>
      timers.startSingleTimer(StopThis, 5.seconds)
      Behaviors.receiveMessage {
        case Ping(_) =>
          pingCounter = pingCounter + 1
          ctx.log.info("Ping Counter {}", pingCounter)
          Behaviors.same
        case Pong(_) =>
          pongCounter = pongCounter + 1
          ctx.log.info("Pong Counter {}", pongCounter)
          Behaviors.same
        case StopThis =>
          ctx.log.info("StopThis command received")
          ctx.spawnAnonymous(LookupActor()) ! LookUpForActors
          Behaviors.same
        case _ =>
          ctx.log.info("I am not supposed to receive this message :(")
          Behaviors.same
      }
    }
  }
  
}
