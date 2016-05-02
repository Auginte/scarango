package com.auginte.scarango.common

/**
  * Predefined collection statuses
  */
class CollectionStatus(val value: Int) extends AnyVal

object CollectionStatuses {
  val New = new CollectionStatus(1)
  val Unloaded = new CollectionStatus(2)
  val Loaded = new CollectionStatus(3)
  val BeingUnloaded = new CollectionStatus(4)
  val Deleted = new CollectionStatus(5)
  val Loading = new CollectionStatus(6)
}