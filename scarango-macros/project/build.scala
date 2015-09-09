import sbt.Keys._
import sbt._

object build extends sbt.Build {
  val buildName = "scarango-macros"
  val buildVersion = "0.2.2"
  val buildScalaVersion = "2.11.7"
  val buildOptions = Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

  val buildDependencies = Seq(
    "io.spray" %%  "spray-json" % "1.3.2",
    "org.scala-lang" % "scala-compiler" % buildScalaVersion
  )

  lazy val macrosSettings = Seq(
    name := buildName,
    version := buildVersion,
    description := "Scala Macro part for scarango driver",
    scalaVersion := buildScalaVersion,
    scalacOptions := buildOptions,
    libraryDependencies ++= buildDependencies,
    scalacOptions in(Compile, doc) ++= Seq("-diagrams")
  )
  
  lazy val scarangoMacros = (project in file(".")
    settings (macrosSettings: _*)
    settings (Publish.settings: _*)
    )
  
}
