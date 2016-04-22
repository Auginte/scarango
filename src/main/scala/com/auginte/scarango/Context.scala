package com.auginte.scarango

import akka.actor.ActorSystem
import akka.stream._
import com.auginte.scarango.state.Authorisation
import scala.concurrent.duration._

/**
  * Parameters for Scarango driver
  */
case class Context(endpoint: String = Context.defaultEndpoint, authorisation: Authorisation = Authorisation.default)
                  (val actorSystem: ActorSystem, val materializer: Materializer, val waitTime: Duration)

object Context {
  val defaultEndpoint = "127.0.0.1:8529"

  private lazy val defaultSystem = ActorSystem("scarango")

  lazy val stopOnError: Supervision.Decider = {
    case some =>
      println("Directive")
      Supervision.Stop
  }

  val decider: Supervision.Decider = { e =>
    println("Unhandled exception in stream", e)
    Supervision.Stop
  }

  val default = fromActorSystem(defaultSystem)

  def fromActorSystem(actorSystem: ActorSystem) = {
    val materializerSettings = ActorMaterializerSettings(actorSystem).withSupervisionStrategy(decider)
    new Context()(
      actorSystem,
      ActorMaterializer(materializerSettings)(actorSystem),
      4.seconds
    )
  }
}
