package com.knoldus.actor

import akka.actor.testkit.typed.scaladsl.{ActorTestKit, TestProbe}
import akka.actor.typed.ActorRef
import com.knoldus.{PingCommand, PingPongCommand, PongCommand}
import org.scalatest.matchers._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.{AnyWordSpec, AnyWordSpecLike}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}

class PingActorSpec extends AnyWordSpec with BeforeAndAfterAll with BeforeAndAfterEach with Matchers {
  val testKit = ActorTestKit()
  
  var counterProbe: TestProbe[PingPongCommand] = _
  var pingActor:ActorRef[PingCommand] = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    counterProbe = testKit.createTestProbe[PingPongCommand]()
    pingActor = testKit.spawn(PingActor.apply(counterProbe.ref))
  }

  "PingActor" must {

    "be able to handle Ping command" in {
      val pongActorProbe = testKit.createTestProbe[PongCommand]()

      val cmd = PingCommand.Ping(pongActorProbe.ref)
      pingActor ! cmd

      counterProbe.expectMessage(cmd)
      pongActorProbe.expectMessage(PongCommand.Pong(pingActor))
    }
    
    "be able to stop" in {
      pingActor ! PingCommand.StopPinging
      counterProbe.expectTerminated(pingActor)
    }

  }

  override def afterAll(): Unit = testKit.shutdownTestKit()
}
