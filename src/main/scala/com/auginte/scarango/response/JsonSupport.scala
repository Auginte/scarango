package com.auginte.scarango.response

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val versionFormat = jsonFormat2(raw.Version)
}