package com.auginte.scarango

import akka.actor.{Actor, ActorSystem, Props}
import com.auginte.scarango.errors.ScarangoError
import com.auginte.scarango.response.Response

/**
 * Class for faster testing
 */
object Main extends App {
  class Client extends Actor {
    override def receive: Receive = {
      case "start" =>
        val db = system.actorOf(Props[Scarango])
        db ! get.Version()

      case Response(v: response.Version, _) =>
        println("[OK] Got version: " + v.version)
        context.system.shutdown()

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
