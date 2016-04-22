package com.auginte.scarango

import akka.stream.scaladsl._

import scala.concurrent.Await

/**
  * Wrapper for ArrangoDB REST API.
  */
class Scarango(val context: Context = Context.default) {
  implicit val system = context.actorSystem
  implicit val materializer = context.materializer
  implicit val executionContext = system.dispatcher
  implicit val currentContext = context

  object Flows {
    val version = Source.single(request.getVersion)
      .via(state.database)
      .map (response.toVersion)
  }

  object Futures {
    def version() = Flows.version.runWith(Sink.head).flatMap(same => same)
  }

  object Results {
    def version() = Await.result(Futures.version(), context.waitTime)
  }
}
