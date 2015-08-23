package com.auginte.scarango

import akka.actor.{Actor, ActorSystem, Props}
import com.auginte.scarango.common.TestKit
import com.auginte.scarango.errors.ScarangoError
import com.auginte.scarango.response.Response

/**
 * Class for faster testing
 */
object Main extends App {
  class Client extends Actor {

    val dbName = TestKit.unique
    
    // Not keeping alive after all data is received
    private var receivedCount: Int = 0

    private def received(): Unit = {
      receivedCount = receivedCount + 1
      if (receivedCount == 5) {
        context.system.shutdown()
      }
    }

    override def receive: Receive = {
      case "start" =>
        val db = system.actorOf(Props[Scarango])
        db ! get.Version
        db ! create.Database(dbName)
        db ! get.Databases
        db ! remove.Database(dbName)
        db ! get.Databases

      case Response(v: response.Version, _) =>
        println("[OK] Got version: " + v.version)
        received()

      case Response(d: response.Database, _) =>
        println("[OK] Created: " + d.name)
        received()

      case Response(d: response.Databases, _) =>
        println("[OK] Got Databases: " + d.result.mkString(", "))
        received()

      case Response(response.Removed(remove.Database(name), _), _) =>
        println("[OK] Removed database: " + name)
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
