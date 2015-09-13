import sbt.Keys._
import sbt._

object build extends sbt.Build {
  val buildName = "scarango"
  val buildVersion = "0.2.4-SNAPSHOT"
  val buildScalaVersion = "2.11.7"
  val buildOptions = Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

  val akkaVersion = "2.3.12"
  val sprayVersion = "1.3.3"
  val buildDependencies = Seq(
    "com.typesafe.akka" % "akka-actor_2.11" % akkaVersion,
    "io.spray" %% "spray-can" % sprayVersion,
    "io.spray" %% "spray-routing" % sprayVersion,
    "io.spray" %% "spray-client" % sprayVersion,
    "io.spray" %%  "spray-json" % "1.3.2",
    "io.spray" %% "spray-json" % "1.3.2",
    "org.scala-lang" % "scala-compiler" % buildScalaVersion,
    "com.auginte" %% "scarango-macros" % "0.2.2", // See scarango-macros folder
    "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
  )

  val scarangoAtSonatype = Seq(
    "OSS" at "https//oss.sonatype.org/content/groups/public"
  )

  lazy val scarangoSettings = Seq(
    name := buildName,
    description := "Scala driver for ArangoDB",
    version := buildVersion,
    scalaVersion := buildScalaVersion,
    scalacOptions := buildOptions,
    mainClass := Some("com.auginte.scarango.Main"),
    resolvers ++= scarangoAtSonatype,
    libraryDependencies ++= buildDependencies,
    scalacOptions in(Compile, doc) ++= Seq("-diagrams"),
    spray.revolver.RevolverPlugin.Revolver.settings
  )

  lazy val scarango = (project in file(".")
    settings (scarangoSettings: _*)
    settings (Publish.settings: _*)
    )
}
