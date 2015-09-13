package com.auginte.scarango

import spray.json.DefaultJsonProtocol._
import spray.json._

/**
 * Package contains types and helpers to simulate state between request-response of ArangoDB
 *
 * == Use cases ==
 *
 * * Connect to needed database and do all operations on that 1 database. No need to write `dbName` every time
 * * Load collection and do multiple operations on that 1 collection. No need to write `collectionName` every time
 *
 */
package object state {
  implicit class DatabaseName(val name: String) extends AnyVal {
    override def toString: String = name
  }

  implicit class CollectionName(val name: String) extends AnyVal {
    override def toString: String = name
  }


  implicit val databaseName2Json: JsonFormat[DatabaseName] = extendedString2Json(DatabaseName, _.name)

  implicit val collectionName2Json: JsonFormat[CollectionName] = extendedString2Json(CollectionName, _.name)


  private def extendedString2Json[A](string2A: String => A, a2String: A => String): JsonFormat[A] = new JsonFormat[A] {
    override def write(obj: A): JsValue = JsString(a2String(obj))

    override def read(json: JsValue): A = json match {
      case JsString(x) => string2A(x)
      case x => deserializationError("Expected String as JsString, but got " + x)
    }
  }
}
