import Dependencies._

val dottyVersion = "3.0.0-M3"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

lazy val root = project
  .in(file("."))
  .settings(
    name := "akka-typed-scala3",
    version := "0.1.0",
    scalaVersion := dottyVersion,
    libraryDependencies ++= Seq(scalaTest) ++ Seq(akkaTyped, logback, akkaTypedTest).map(_.withDottyCompat(scalaVersion.value))
  )
