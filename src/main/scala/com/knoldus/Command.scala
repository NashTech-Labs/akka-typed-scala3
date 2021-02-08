package com.knoldus

import akka.actor.typed.ActorRef
import akka.actor.typed.receptionist.Receptionist

sealed trait Command

enum PingCommand extends Command {
  case Ping(actor: ActorRef[PongCommand])
  case StopPinging
}

enum PongCommand extends Command {
  case Pong(actor: ActorRef[PingCommand])
  case StopPonging
}

enum LookupCommand {
  case ListingResponse(listing: Receptionist.Listing )
  case LookUpForActors
}

type PingPongCommand = PingCommand | PongCommand
