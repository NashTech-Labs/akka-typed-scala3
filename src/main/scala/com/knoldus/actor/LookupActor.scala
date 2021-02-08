package com.knoldus.actor

import com.knoldus._
import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import com.knoldus.LookupCommand

object LookupActor {

  import LookupCommand._

  def apply(): Behavior[LookupCommand] = Behaviors.receive { (ctx, msg) =>
    val listingResponseAdapter: ActorRef[Receptionist.Listing] = ctx.messageAdapter(ListingResponse(_))
    msg match {
      case LookUpForActors =>
        ctx.log.warn("Lookup started for PingActor..")
        ctx.system.receptionist ! Receptionist.Find(PingActor.PingServiceKey, listingResponseAdapter)
        ctx.log.warn("Lookup started for PongActor..")
        ctx.system.receptionist ! Receptionist.Find(PongActor.pongServiceKey, listingResponseAdapter)
        Behaviors.same
      case ListingResponse(PingActor.PingServiceKey.Listing(listings)) =>
        ctx.log.warn("Stopping Ping Actor {}", listings)
        listings.foreach(ref => ref ! PingCommand.StopPinging)
        Behaviors.same
      case ListingResponse(PongActor.pongServiceKey.Listing(listings)) =>
        listings.foreach(ref => ref ! PongCommand.StopPonging)
        Behaviors.stopped
      case ListingResponse(_) =>
        Behaviors.stopped
    }

  }
}
