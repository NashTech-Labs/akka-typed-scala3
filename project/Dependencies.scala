import sbt._

object Dependencies {
  private val AkkaVersion = "2.6.12"

  val akkaTyped = "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion

  val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"

  val akkaTypedTest = "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test
}
