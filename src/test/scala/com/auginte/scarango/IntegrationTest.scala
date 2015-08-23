package com.auginte.scarango

import akka.actor._
import com.auginte.scarango.common.TestKit
import com.auginte.scarango.errors.ScarangoError
import com.auginte.scarango.helpers.AkkaSpec
import com.auginte.scarango.response.{Database, Databases, Response, Version}

import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * Testing integration with ArangoDB
 */
class IntegrationTest extends AkkaSpec {
  "In environment with real ArangoDB instance, driver" should {
    "get version of ArangoDB" in {
      val system = akkaSystem("TestVersions")
      var version: Option[Version] = None

      class ClientGetVersion extends Actor {
        override def receive: Receive = {
          case "start" =>
            val db = system.actorOf(Props(new Scarango()))
            db ! get.Version

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

      system.awaitTermination(10 seconds)
      assert(version.isDefined)
      info("Version: " + version.get.version)

      assert(version.get.version === "2.6.5")
    }
    "get multiple results: version and database list" in {
      val system = akkaSystem("TestMultiple")
      var version: Option[Version] = None
      var databases: Option[Databases] = None

      class ClientMultiple extends Actor {
        override def receive: Receive = {
          case "start" =>
            val db = system.actorOf(Props(new Scarango()))
            db ! get.Version
            db ! get.Databases

          case Response(v: response.Version, _) =>
            version = Some(v)

          case Response(d: response.Databases, _) =>
            databases = Some(d)
            system.shutdown() // Testing, if it is second

          case e: ScarangoError =>
            fail("[ScarangoError] " + e.getMessage)
            context.system.shutdown()

          case other =>
            fail("[UNEXPECTED] " + other)
            context.system.shutdown()
        }
      }

      val client = system.actorOf(Props(new ClientMultiple()))
      client ! "start"

      system.awaitTermination(10 seconds)
      assert(version.isDefined)
      info("Version: " + version.get.version)
      assert(version.get.version === "2.6.5")

      assert(databases.isDefined)
      info("Databases: " + databases.get.result.mkString(", "))
      assert(databases.get.result.contains("_system"))
      assert(databases.get.code === 200)
      assert(!databases.get.error)
    }

    "create new database" in {
      val system = akkaSystem("TestCreateDatabase")
      val dbName = TestKit.unique
      var database: Option[Database] = None
      var databases: Option[Databases] = None

      class ClientMultiple extends Actor {
        override def receive: Receive = {
          case "start" =>
            val db = system.actorOf(Props(new Scarango()))
            db ! create.Database(dbName)
            db ! get.Databases

          case Response(d: response.Database, _) =>
            database = Some(d)

          case Response(d: response.Databases, _) =>
            databases = Some(d)
            system.shutdown() // Testing, if it is second

          case e: ScarangoError =>
            fail("[ScarangoError] " + e.getMessage)
            context.system.shutdown()

          case other =>
            fail("[UNEXPECTED] " + other)
            context.system.shutdown()
        }
      }

      val client = system.actorOf(Props(new ClientMultiple()))
      client ! "start"

      system.awaitTermination(10 seconds)
      assert(database.isDefined)
      assert(database.get.name === dbName)
      assert(database.get.raw.result === true)
      info("New database: " + database.get.name)

      assert(databases.isDefined)
      info("Databases: " + databases.get.result.mkString(", "))
      assert(databases.get.result.contains("_system"))
      assert(databases.get.result.contains(dbName))
      assert(databases.get.code === 200)
      assert(!databases.get.error)
    }

  }
}