package com.auginte.scarango.response.meta.collection

/**
 * General structure for sealed class based enumeration
 */
trait Enum[A <: WithId] {
  val valid: Seq[A]

  def unknown(id: Int): A

  def byValue(id: Int): A = valid find (_.id == id) getOrElse unknown(id)
}
