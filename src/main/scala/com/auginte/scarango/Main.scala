package com.auginte.scarango

import akka.actor.{Actor, ActorSystem, Props}
import com.auginte.scarango.common.TestKit
import com.auginte.scarango.errors.ScarangoError
import com.auginte.scarango.request._
import com.auginte.scarango.response._

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
        db ! GetVersion
        db ! CreateDatabase(dbName)
        db ! CreateCollection(collectionName)
        db ! RemoveCollection(collectionName)
        db ! GetDatabases
        db ! RemoveDatabase(dbName)
        db ! GetDatabases

      case ResponseIdentifier(v: Version, _) =>
        println("[OK] Got version: " + v.version)
        received()

      case ResponseIdentifier(d: DatabaseCreated, _) =>
        println("[OK] Created: " + d.name)
        received()

      case ResponseIdentifier(d: Databases, _) =>
        println("[OK] Got Databases: " + d.result.mkString(", "))
        received()

      case ResponseIdentifier(DatabaseRemoved(RemoveDatabase(name), _), _) =>
        println("[OK] Removed database: " + name)
        received()

      case ResponseIdentifier(c: CollectionCreated, _) =>
        println("[OK] Collection created: " + c.name + " with id " + c.id)
        received()

      case ResponseIdentifier(CollectionRemoved(RemoveCollection(name), raw), _) =>
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
