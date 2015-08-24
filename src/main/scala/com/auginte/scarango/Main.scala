package com.auginte.scarango

import akka.actor.{Actor, ActorSystem, Props}
import com.auginte.scarango.common.TestKit
import com.auginte.scarango.errors.ScarangoError
import com.auginte.scarango.response.created.{Collection, Database}
import com.auginte.scarango.response.existing.{Databases, Version}
import com.auginte.scarango.response.{Response, removed}

/**
 * Class for faster testing
 */
object Main extends App {
  class Client extends Actor {

    val dbName = TestKit.unique
    val collectionName = TestKit.unique

    // Not keeping alive after all data is received
    private var receivedCount: Int = 0

    private def received(): Unit = {
      receivedCount = receivedCount + 1
      if (receivedCount == 7) {
        context.system.shutdown()
      }
    }

    override def receive: Receive = {
      case "start" =>
        val db = system.actorOf(Props[Scarango])
        db ! get.Version
        db ! create.Database(dbName)
        db ! create.Collection(collectionName)
        db ! remove.Collection(collectionName)
        db ! get.Databases
        db ! remove.Database(dbName)
        db ! get.Databases

      case Response(v: Version, _) =>
        println("[OK] Got version: " + v.version)
        received()

      case Response(d: Database, _) =>
        println("[OK] Created: " + d.name)
        received()

      case Response(d: Databases, _) =>
        println("[OK] Got Databases: " + d.result.mkString(", "))
        received()

      case Response(removed.Database(remove.Database(name), _), _) =>
        println("[OK] Removed database: " + name)
        received()

      case Response(c: Collection, _) =>
        println("[OK] Collection created: " + c.name + " with id " + c.id)
        received()

      case Response(removed.Collection(remove.Collection(name), raw), _) =>
        println("[OK] Collection removed: " + name + " with id " + raw.id)
        received()

      case e: ScarangoError =>
        println("[ERROR] " + e.getMessage)
        context.system.shutdown()

      case other =>
        println("[UNEXPECTED] " + other)
        context.system.shutdown()
    }
  }

  implicit val system = ActorSystem("Scarango")
  val client = system.actorOf(Props[Client])
  client ! "start"

}
