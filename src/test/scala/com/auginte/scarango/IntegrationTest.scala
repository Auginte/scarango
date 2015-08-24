package com.auginte.scarango

import akka.actor._
import com.auginte.scarango.common.TestKit
import com.auginte.scarango.errors.ScarangoError
import com.auginte.scarango.helpers.AkkaSpec
import com.auginte.scarango.response._
import com.auginte.scarango.response.created.Database
import com.auginte.scarango.response.existing.{Databases, Version}

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

          case Response(v: Version, _) =>
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

          case Response(v: Version, _) =>
            version = Some(v)

          case Response(d: Databases, _) =>
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
      var databaseCreated: Option[Database] = None
      var databaseRemoved: Option[removed.Database] = None
      var databases1: Option[Databases] = None
      var databases2: Option[Databases] = None
      var fetchCount = 0

      class ClientDatabases extends Actor {
        override def receive: Receive = {
          case "start" =>
            val db = system.actorOf(Props(new Scarango()))
            db ! create.Database(dbName)
            db ! get.Databases
            db ! remove.Database(dbName)
            db ! get.Databases

          case Response(d: Database, _) =>
            databaseCreated = Some(d)

          case Response(d: Databases, _) if fetchCount == 0 =>
            databases1 = Some(d)
            fetchCount = fetchCount + 1

          case Response(r: removed.Database, _) =>
            databaseRemoved = Some(r)

          case Response(d: Databases, _) if fetchCount == 1 =>
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

      val client = system.actorOf(Props(new ClientDatabases()))
      client ! "start"

      system.awaitTermination(10 seconds)
      assert(databaseCreated.isDefined)
      assert(databaseCreated.get.name === dbName)
      assert(databaseCreated.get.raw.result === true)
      info("New database: " + databaseCreated.get.name)

      assert(databases1.isDefined)
      info("Databases: " + databases1.get.result.mkString(", "))
      assert(databases1.get.result.contains("_system"))
      assert(databases1.get.result.contains(dbName))
      assert(databases1.get.code === 200)
      assert(!databases1.get.error)

      assert(databaseRemoved.isDefined)
      info("Removed database: " + databaseRemoved.get.element.name)
      assert(databaseRemoved.get.element.name === dbName)
      assert(databaseRemoved.get.raw.code === 200)
      assert(databaseRemoved.get.raw.error === false)
      assert(databaseRemoved.get.raw.result === true)

      assert(databases2.isDefined)
      assert(fetchCount === 2)
      info("Databases: " + databases2.get.result.mkString(", "))
      assert(databases2.get.result.contains("_system"))
      assert(!databases2.get.result.contains(dbName))
      assert(databases2.get.code === 200)
      assert(!databases2.get.error)
    }

    "create and drop collection in _system database" in {
      val system = akkaSystem("TestCreateCollection")
      val collectionName = TestKit.unique
      var collectionCreated: Option[created.Collection] = None
      var collectionRemoved: Option[removed.Collection] = None

      class ClientCollections extends Actor {
        override def receive: Receive = {
          case "start" =>
            val db = system.actorOf(Props(new Scarango()))
            db ! create.Collection(collectionName)
            db ! remove.Collection(collectionName)

          case Response(c: created.Collection, _) =>
            collectionCreated = Some(c)

          case Response(r: removed.Collection, _) =>
            collectionRemoved = Some(r)
            system.shutdown()

          case e: ScarangoError =>
            fail("[ScarangoError] " + e.getMessage)
            context.system.shutdown()

          case other =>
            fail("[UNEXPECTED] " + other)
            context.system.shutdown()
        }
      }

      val client = system.actorOf(Props(new ClientCollections()))
      client ! "start"

      system.awaitTermination(10 seconds)
      assert(collectionCreated.isDefined)
      assert(collectionCreated.get.name === collectionName)
      assert(collectionCreated.get.id.length > 3)
      assert(collectionCreated.get.error === false)
      assert(collectionCreated.get.code === 200)
      info("New collection: " + collectionCreated.get.name)
      info("With id: " + collectionCreated.get.id)

      assert(collectionRemoved.isDefined)
      info("Removed colection: " + collectionRemoved.get.element.name)
      info("With id: " + collectionRemoved.get.raw.id)
      assert(collectionRemoved.get.element.name === collectionName)
      assert(collectionRemoved.get.raw.id === collectionCreated.get.id)
      assert(collectionRemoved.get.raw.code === 200)
      assert(collectionRemoved.get.raw.error === false)
    }
  }
}