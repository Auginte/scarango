package com.auginte.scarango.errors

import akka.util.ByteString
import com.auginte.scarango.Context

import scala.concurrent.Await

/**
  * Exceptions related to Scarango driver
  */
abstract class ScarangoException(message: String, cause: Throwable = null)(implicit context: Context) extends Exception(message, cause) with ResponseData {
  def causeOption: Option[Throwable] = Option(getCause)

  implicit val materializer = context.materializer
  implicit val executionContext = context.actorSystem.dispatcher

  private lazy val responseText = try {
    Await.result(httpResponse.entity.dataBytes.runFold(ByteString(""))(_ ++ _).map(_.utf8String), context.waitTime)
  } catch {
    case e: Exception => "Scarango internal: Response was too big"
  }

    override def getMessage: String = super.getMessage + " Response: " + responseText
}
