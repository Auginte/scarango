package com.auginte.scarango.helpers

import akka.actor._
import akka.event.Logging
import akka.event.Logging.{Error, InitializeLogger, LogEvent, LogLevel, LoggerInitialized}

import scala.concurrent.duration._
import scala.language.postfixOps

class ScalaTestLogger extends Actor {
  override def receive: Receive = {
    case InitializeLogger(_) => sender() ! LoggerInitialized
    case event: LogEvent => event.level match {
      case Logging.InfoLevel | Logging.DebugLevel => info(event)
      case other => error(event)
    }
  }

  private def info(event: LogEvent): Unit = ScalaTestLogger.proxy match {
    case Some(proxy) => proxy.info(s"[${level(event.level)}] ${event.message}")
    case None => println(s"          [${level(event.level)}] ${event.message}")
  }

  private def error(event: LogEvent): Unit = ScalaTestLogger.proxy match {
    case Some(proxy) =>
      event match {
        case e: Error =>
          proxy.alert(e.message.toString)
          proxy.alert(e.cause.getStackTrace.mkString("\n  + "))
        case other =>
          proxy.alert(s"[${level(other.level)}] ${other.message}")
      }
      val a = event
      proxy.lastSystem.foreach { system =>
        system.shutdown()
        system.awaitTermination(10 seconds)
      }
      proxy.fail("Error in logs")
    case None =>
      println(s"         [${level(event.level)}] ${event.message}")
      System.exit(13) // Akka cache all errors. Needed for CI systems to understand failed tests

  }

  private def level(l: LogLevel) = l match {
    case Logging.DebugLevel => "DEBUG"
    case Logging.InfoLevel => "INFO"
    case Logging.WarningLevel => "WARN"
    case Logging.ErrorLevel => "ERROR"
    case _ => "UNEXPECTED"
  }
}

object ScalaTestLogger {
  var proxy: Option[AkkaSpec] = None
}