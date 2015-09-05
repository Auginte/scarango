package com.auginte.scarango.response.meta.collection

/**
 * Collection result with human readable `status` codes
 */
trait EnumStatuses {
  val status: Int

  def enumStatus: Status = Statuses.byValue(status)
}
