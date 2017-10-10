import sbt._

object Dependencies {
  // Versions
  lazy val akkaVersion = "2.4.18"

  // Libraries
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaVersion

  val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.0.8"
  val akkaSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.8"

  val slick = "com.typesafe.slick" %% "slick" % "3.2.1"
  val h2db = "com.h2database" % "h2" % "1.4.185"
  val logback = "ch.qos.logback" % "logback-classic" % "1.1.2"


  val scalactic = "org.scalactic" %% "scalactic" % "3.0.1"
  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"
  val akkaHttpTestKit = "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10"


  // Projects
  val backendDeps =
    Seq(akkaActor,
      akkaHttp,
      akkaSprayJson,
      slick,
      h2db,
      logback,
      scalaTest % Test,
      scalactic % Test,
      akkaHttpTestKit % Test)
}
