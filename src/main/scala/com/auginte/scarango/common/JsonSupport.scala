package com.auginte.scarango.common

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit object CollectionTypeFormat extends RootJsonFormat[CollectionType] {
    def write(c: CollectionType) = new JsNumber(c.value)

    def read(value: JsValue) = value match {
      case JsNumber(n) if CollectionTypes.validValues.contains(n) => new CollectionType(n.toInt)
      case _ => deserializationError("CollectionType should be one of: " + CollectionTypes.validValues.mkString(", "))
    }
  }
  implicit object CollectionStatusFormat extends RootJsonFormat[CollectionStatus] {
    def write(c: CollectionStatus) = new JsNumber(c.value)

    def read(value: JsValue) = value match {
      case JsNumber(n) if n > 0 => new CollectionStatus(n.toInt)
      case _ => deserializationError("CollectionStatus should be positive number")
    }
  }
}