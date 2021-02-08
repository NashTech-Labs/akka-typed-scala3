package com.knoldus.actor

import akka.actor.testkit.typed.scaladsl.{ActorTestKit, TestProbe}
import akka.actor.typed.ActorRef
import com.knoldus.{PingCommand, PingPongCommand, PongCommand}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.{AnyWordSpec, AnyWordSpecLike}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}

class PongActorSpec extends AnyWordSpec with BeforeAndAfterAll with BeforeAndAfterEach with Matchers {
  val testKit = ActorTestKit()

  var counterProbe: TestProbe[PingPongCommand] = _
  var pongActor: ActorRef[PongCommand] = _

  override def beforeEach(): Unit = {
    super.beforeEach()
    counterProbe = testKit.createTestProbe[PingPongCommand]()
    pongActor = testKit.spawn(PongActor.apply(counterProbe.ref))
  }
  
  "PongActor" must {
    "be able to handle Pong command" in {
      val pingActorProbe = testKit.createTestProbe[PingCommand]()
      
      val cmd = PongCommand.Pong(pingActorProbe.ref)
      pongActor ! cmd

      counterProbe.expectMessage(cmd)
      pingActorProbe.expectMessage(PingCommand.Ping(pongActor))
    }
  }

}
