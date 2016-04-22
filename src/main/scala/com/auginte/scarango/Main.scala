package com.auginte.scarango

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
  * Class for faster testing
  */
object Main extends App {
  println("Connecting to ArangoDB...")

  val context = Context.default
  implicit val executionContext = context.actorSystem.dispatcher

  val scarango = new Scarango(context)
  val version = scarango.Futures.version()
  version.onComplete {
    case Success(v) =>
      println(s"Arango db version: ${v.version}")
      context.actorSystem.terminate()
    case Failure(error) =>
      println(s"Failed to get version: $error")
      context.actorSystem.terminate()
  }
  Await.result(version, 4.seconds)

}
