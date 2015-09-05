package com.auginte.scarango.response.meta.collection

/**
 * Human readable collection statuses
 */
object Statuses extends Enum[Status] {

  case object New extends Status(1, "new born collection")

  case object Unloaded extends Status(2, "unloaded")

  case object Loaded extends Status(3, "loaded")

  case object BeingUnloaded extends Status(4, "in the process of being unloaded")

  case object Deleted extends Status(5, "deleted")

  case object Loading extends Status(6, "new born collection")

  override def unknown(id: Int): Status = new Status(id, s"unknown ($id)")

  val valid = Seq(New, Unloaded, Loaded, BeingUnloaded, Deleted, Loaded)
}

sealed class Status(val id: Int, val name: String)