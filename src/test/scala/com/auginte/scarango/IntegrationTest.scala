package com.auginte.scarango

import akka.actor._
import com.auginte.scarango.errors.ScarangoError
import com.auginte.scarango.helpers.AkkaSpec
import com.auginte.scarango.response.{Response, Version}

import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * Testing integration with ArangoDB
 */
class IntegrationTest extends AkkaSpec {
  "In environment with real ArangoDB instance, driver" should {
    "get version of ArangoDB" in {
      var version: Option[Version] = None

      class ClientGetVersion extends Actor {
        override def receive: Receive = {
          case "start" =>
            val db = system.actorOf(Props(new Scarango()))
            db ! get.Version()

          case Response(v: response.Version, _) =>
            version = Some(v)
            context.system.shutdown()

          case e: ScarangoError =>
            fail("[ScarangoError] " + e.getMessage)
            context.system.shutdown()

          case other =>
            fail("[UNEXPECTED] " + other)
            context.system.shutdown()
        }
      }

      val client = system.actorOf(Props(new ClientGetVersion()))
      client ! "start"

      system.awaitTermination(2 seconds)
      assert(version.isDefined)
      info("Version: " + version.get.version)

      if (version.get.version !== "2.6.4") { // Hack, until newest ArangoDB TravisCI version will be deployed
        assert(version.get.version === "2.6.5")
      }
    }
  }
}