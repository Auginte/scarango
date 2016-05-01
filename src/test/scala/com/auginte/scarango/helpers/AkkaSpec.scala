package com.auginte.scarango.helpers

import akka.actor._
import com.auginte.scarango.response.raw
import com.auginte.scarango.{Context, Scarango}
import com.typesafe.config.ConfigFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
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

  def part(message: String)(f: => Any): Unit = {
    info(message)
    f
  }
  def sub(message: String)(f: => Any): Unit = part("  " + message)(f)

  def subCommented[A](message: String)(f: => A): A = commented[A]("  " + message)(f)

  def commented[A](message: String)(f: => A): A = {
    info(message)
    f
  }

  def context(message: String) = info("\t" + message)

  override def info: Informer = super.info

  override def note: Notifier = super.note

  override def alert: Alerter = super.alert

  //
  // Common functions
  //

  protected def withDriver(testCode: Scarango => Any): Unit = testCode(new Scarango(defaultConfig))

  protected def randomId = "-" + System.currentTimeMillis + "-" + scala.util.Random.nextInt(9999)

  val contains = (list: List[raw.list.Collection], name: String) => list.count(_.name == name) == 1
  val byName = (list: List[raw.list.Collection], name: String) => list.filter(_.name == name).head

  object HttpStatusCodes {
    val ok = 200
    val created = 201
  }
}

