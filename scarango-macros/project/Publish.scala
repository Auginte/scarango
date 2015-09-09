import sbt._
import Keys._

object Publish {
  val nexus = "https://oss.sonatype.org/"

  lazy val settings = Seq(
    organization := "com.auginte",
    licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    homepage := Some(url("http://scarango.auginte.com/")),

    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := (_ => false),

    publishTo <<= version { v =>
      if (v.trim endsWith "SNAPSHOT")
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },

    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),

    pomExtra :=
        <scm>
          <url>git@github.com:aurelijusb/scarango.git/scarango-macros</url>
          <connection>scm:git@github.com:aurelijusb/scarango.git/scarango-macros</connection>
        </scm>
        <developers>
          <developer>
            <id>aurelijusb</id>
            <name>Aurelijus Banelis</name>
            <url>http://aurelijus.banelis.lt</url>
          </developer>
        </developers>
  )
}