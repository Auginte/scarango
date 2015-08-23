package com.auginte.scarango.helpers

import akka.actor._
import com.typesafe.config.ConfigFactory
import org.scalatest.{Alerter, Informer, Notifier, WordSpec}

import scala.language.postfixOps

trait AkkaSpec extends WordSpec {
  ScalaTestLogger.proxy = Some(this)

  def testConfig = ConfigFactory.parseString(
    """
      |akka.loggers = ["com.auginte.scarango.helpers.ScalaTestLogger"]
    """.stripMargin
  )

  var lastSystem: Option[ActorSystem] = None

  def akkaSystem(name: String = "TestSystem"): ActorSystem = {
    val system = ActorSystem(name, testConfig)
    lastSystem = Some(system)
    system
  }

  override def info: Informer = super.info

  override def note: Notifier = super.note

  override def alert: Alerter = super.alert
}

