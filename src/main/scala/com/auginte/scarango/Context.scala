package com.auginte.scarango

import akka.actor.ActorSystem
import akka.stream._
import com.auginte.scarango.request.raw.create.User
import com.auginte.scarango.state.Authorisation

import scala.concurrent.duration._

/**
  * Parameters for Scarango driver
  */
case class Context(
                    endpoint: String = Context.defaultEndpoint,
                    authorisation: Authorisation = Authorisation.default,
                    database: String = Context.defaultDatabase
                  )
                  (val actorSystem: ActorSystem = Context.defaultActorSystem, val materializer: Materializer = Context.defaultMaterializer, val waitTime: Duration = Context.defaultWaitDuration) {

  private def curryCopy(partial: (ActorSystem, Materializer, Duration) => Context): Context = partial(actorSystem, materializer, waitTime)

  def withDatabase(newName: String) = curryCopy(copy(database = newName))

  def withAuthorisation(user: User) = curryCopy(copy(authorisation = Authorisation.forUser(user)))
}

object Context {
  val defaultEndpoint = "127.0.0.1:8529"
  val defaultDatabase = "_system"
  val defaultWaitDuration = 4.seconds

  private[scarango] lazy val defaultActorSystem = ActorSystem("scarango")
  private[scarango] lazy val defaultMaterializer = ActorMaterializer()(defaultActorSystem)

  val default = fromActorSystem(defaultActorSystem)

  def fromActorSystem(actorSystem: ActorSystem) = {
    val materializerSettings = ActorMaterializerSettings(actorSystem)
    new Context()(
      actorSystem,
      ActorMaterializer(materializerSettings)(actorSystem),
      defaultWaitDuration
    )
  }
}
