import sbt.Keys._
import sbt._

object build extends sbt.Build {
  val buildName = "scarango-macros"
  val buildVersion = "0.1"
  val buildScalaVersion = "2.11.7"
  val buildOptions = Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

  val buildDependencies = Seq(
    "io.spray" %%  "spray-json" % "1.3.2",
    "org.scala-lang" % "scala-compiler" % buildScalaVersion
  )

  lazy val scarangoLibrary = Project(id = buildName, base = file(".")) settings
    (
      name := buildName,
      version := buildVersion,
      scalaVersion := buildScalaVersion,
      scalacOptions := buildOptions,
      libraryDependencies ++= buildDependencies,
      scalacOptions in(Compile, doc) ++= Seq("-diagrams")
    )
}
