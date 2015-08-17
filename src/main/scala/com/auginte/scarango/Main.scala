package com.auginte.scarango

import akka.actor.{Props, ActorSystem, PoisonPill, Actor}
import akka.io.IO
import akka.io.Tcp.{Close, ErrorClosed}
import spray.can.Http
import spray.http._

/**
 * Class for faster testing
 */
object Main extends App {
  class MyActor extends Actor {
    override def receive: Receive = {
      case "start" =>
        implicit val system = context.system
        IO(Http) ! Http.Connect("127.0.0.1", port = 8529)
      case Http.Connected(remote, local) =>
        sender() ! request.Version().http
      case HttpResponse(status, entry, headers, protocol) if status.isSuccess =>
        println(s"$status: ${entry.data.asString}")
        sender() ! Close
        context.system.shutdown()
      case e:ErrorClosed =>
        println(e)
        context.system.shutdown()
      case a: Any =>
        println(a)
        context.system.shutdown()
    }
  }


  implicit val system = ActorSystem("Scarango")

  val actor = system.actorOf(Props[MyActor])
  actor ! "start"
}
