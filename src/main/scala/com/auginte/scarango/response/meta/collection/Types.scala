package com.auginte.scarango.response.meta.collection

sealed class Type(val id: Int, val name: String) extends WithId

/**
 * Human readable collection statuses
 */
object Types extends Enum[Type] {

  case object Document extends Type(2, "document collection")

  case object Edge extends Type(3, "edges collection")

  override def unknown(id: Int): Type = new Type(id, s"unknown ($id)")

  override val valid: Seq[Type] = Seq(Document, Edge)
}