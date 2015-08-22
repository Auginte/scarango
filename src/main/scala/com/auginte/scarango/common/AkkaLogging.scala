package com.auginte.scarango.common

import akka.actor.Actor
import akka.event.Logging

import scala.annotation.elidable

/**
 * Logging infrastructure
 */
trait AkkaLogging extends Actor {
  val log = Logging(context.system, this)

  @elidable(elidable.FINER)
  protected def debug(message: String, args: Any*) = log.info(message + args.mkString(" | ", " | ", ""))

  @elidable(elidable.SEVERE)
  protected def error(message: String, args: Any*) = log.error(message + args.mkString(" | ", " | ", ""))
}
