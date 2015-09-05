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

    override def receive: Receive = {
      case "start" =>
        val db = system.actorOf(Props[Scarango])
        db ! GetVersion
        db ! CreateDatabase(dbName)
        db ! CreateCollection(collectionName)
        db ! GetCollection(collectionName)
        db ! RemoveCollection(collectionName)
        db ! request.Identifiable(GetDatabases, id = "with database")
        db ! RemoveDatabase(dbName)
        db ! request.Identifiable(GetDatabases, id = "database removed")

      case v: Version =>
        println("[OK] Got version: " + v.version)

      case d: DatabaseCreated =>
        println("[OK] Created: " + d.name)

      case response.Identifiable(d: Databases, id, _, _, _) if id == "with database" =>
        println("[OK] Got Databases: " + d.result.mkString(", "))

      case response.Identifiable(d: Databases, id, _, _, _) if id == "database removed" =>
        println("[OK] Got updated Databases: " + d.result.mkString(", "))
        context.system.shutdown()

      case DatabaseRemoved(RemoveDatabase(name), _) =>
        println("[OK] Removed database: " + name)

      case c: CollectionCreated =>
        println("[OK] Collection created: " + c.name + " with id " + c.id)

      case c: Collection =>
        println(s"[OK] Collection: {ID: ${c.id} NAME: ${c.name} STATUS: ${c.enumStatus} TYPE: ${c.enumType}}")

      case CollectionRemoved(RemoveCollection(name), raw) =>
        println("[OK] Collection removed: " + name + " with id " + raw.id)

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
