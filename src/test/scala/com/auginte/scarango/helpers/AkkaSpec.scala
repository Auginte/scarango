package com.auginte.scarango.helpers

import akka.actor._
import com.auginte.scarango.{Context, Scarango}
import com.typesafe.config.ConfigFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.words.StringVerbBlockRegistration
import org.scalatest.{Alerter, Informer, Notifier, WordSpec}

import scala.concurrent.Future
import scala.language.postfixOps

trait AkkaSpec extends WordSpec with ScalaFutures {
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


  implicit val defaultPatience =
    PatienceConfig(timeout = Span(4, Seconds), interval = Span(500, Millis))

  def withDelay[A](result: Future[A])(test: A => Any) = whenReady(result)(test)

  lazy val defaultConfig = Context.fromActorSystem(akkaSystem())

  implicit lazy val exectutionContext = defaultConfig.actorSystem.dispatcher

  override def info: Informer = super.info

  override def note: Notifier = super.note

  override def alert: Alerter = super.alert

  //
  // Common functions
  //

  protected def withDriver(testCode: Scarango => Any): Unit = testCode(new Scarango(defaultConfig))

  protected def randomId = "-" + System.currentTimeMillis + "-" + scala.util.Random.nextInt(9999)

  object HttpStatusCodes {
    val ok = 200
    val created = 201
  }
}

