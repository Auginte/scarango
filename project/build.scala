import sbt.Keys._
import sbt._

object build extends sbt.Build {
  val buildName = "scarango"
  val buildVersion = "0.3.1"
  val buildScalaVersion = "2.11.8"
  val buildOptions = Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

  val akkaVersion = "2.4.4"
  val buildDependencies = Seq(
    "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,
    "org.scalatest" % "scalatest_2.11" % "2.2.6" % "test"
  )

  val scarangoAtSonatype = Seq(
    "OSS Nexus" at "https://oss.sonatype.org/content/groups/public"
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

  lazy val scarango = (project in file("."))
    .settings(scarangoSettings: _*)
    .settings(Publish.settings: _*)
}
