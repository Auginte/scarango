package com.auginte.scarango.common

/**
  * Predefined collection types
  */
class CollectionType(val value: Int) extends AnyVal

object CollectionTypes {
  val Document = new CollectionType(2)
  val Edge = new CollectionType(3)

  val validValues = List(Document, Edge).map(_.value)
}

