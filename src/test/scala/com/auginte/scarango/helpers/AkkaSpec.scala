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

  implicit val system:ActorSystem = ActorSystem("TestVersion", testConfig)

  override def info: Informer = super.info

  override def note: Notifier = super.note

  override def alert: Alerter = super.alert
}

