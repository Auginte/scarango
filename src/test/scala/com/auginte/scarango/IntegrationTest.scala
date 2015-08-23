package com.auginte.scarango

import akka.actor._
import com.auginte.scarango.common.TestKit
import com.auginte.scarango.errors.ScarangoError
import com.auginte.scarango.helpers.AkkaSpec
import com.auginte.scarango.response._
import com.auginte.scarango.response.raw.BoolResponse

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

    "create and drop new database" in {
      val system = akkaSystem("TestCreateDatabase")
      val dbName = TestKit.unique
      var database: Option[Database] = None
      var removed: Option[remove.Database] = None
      var removedRaw: Option[BoolResponse] = None
      var databases1: Option[Databases] = None
      var databases2: Option[Databases] = None
      var fetchCount = 0

      class ClientMultiple extends Actor {
        override def receive: Receive = {
          case "start" =>
            val db = system.actorOf(Props(new Scarango()))
            db ! create.Database(dbName)
            db ! get.Databases
            db ! remove.Database(dbName)
            db ! get.Databases

          case Response(d: response.Database, _) =>
            database = Some(d)

          case Response(d: response.Databases, _) if fetchCount == 0 =>
            databases1 = Some(d)
            fetchCount = fetchCount + 1

          case Response(Removed(d: remove.Database, raw), _) =>
            removed = Some(d)
            removedRaw = Some(raw)

          case Response(d: response.Databases, _) if fetchCount == 1 =>
            databases2 = Some(d)
            fetchCount = fetchCount + 1
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

      assert(databases1.isDefined)
      info("Databases: " + databases1.get.result.mkString(", "))
      assert(databases1.get.result.contains("_system"))
      assert(databases1.get.result.contains(dbName))
      assert(databases1.get.code === 200)
      assert(!databases1.get.error)

      assert(removed.isDefined)
      assert(removedRaw.isDefined)
      info("Removed database: " + removed.get.name)
      assert(removed.get.name === dbName)
      assert(removedRaw.get.code === 200)
      assert(removedRaw.get.error === false)
      assert(removedRaw.get.result === true)

      assert(databases2.isDefined)
      assert(fetchCount === 2)
      info("Databases: " + databases2.get.result.mkString(", "))
      assert(databases2.get.result.contains("_system"))
      assert(!databases2.get.result.contains(dbName))
      assert(databases2.get.code === 200)
      assert(!databases2.get.error)
    }

  }
}