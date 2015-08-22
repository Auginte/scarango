import sbt.Keys._
import sbt._

object build extends sbt.Build {
  val buildName = "scarango"
  val buildVersion = "0.1"
  val buildScalaVersion = "2.11.7"
  val buildOptions = Seq("-unchecked", "-deprecation", "-encoding", "utf8")

  val akkaVersion = "2.3.12"
  val sprayVersion = "1.3.3"
  val buildDependencies = Seq(
    "com.typesafe.akka" % "akka-actor_2.11" % akkaVersion,
    "io.spray" %% "spray-can" % sprayVersion,
    "io.spray" %% "spray-routing" % sprayVersion,
    "io.spray" %% "spray-client" % sprayVersion,
    "io.spray" %%  "spray-json" % "1.3.2",
    "io.spray" %% "spray-json" % "1.3.2",
    "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
  )

  lazy val scarangoLibrary = Project(id = buildName, base = file(".")) settings
    (
      name := buildName,
      version := buildVersion,
      scalaVersion := buildScalaVersion,
      scalacOptions := buildOptions,
      mainClass := Some("com.auginte.scarango"),
      libraryDependencies ++= buildDependencies,
      spray.revolver.RevolverPlugin.Revolver.settings
    )
}
