package com.auginte.scarango.response

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.auginte.scarango.common
import spray.json._

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  private object CommonJsonSuppoer extends common.JsonSupport
  import CommonJsonSuppoer._

  implicit val versionFormat = jsonFormat2(raw.Version)
  implicit val createCollectionFormat = jsonFormat9(raw.create.Collection)
  implicit val createDocumentFormat = jsonFormat4(raw.create.Document)
}