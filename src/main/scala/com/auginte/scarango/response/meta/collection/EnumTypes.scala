package com.auginte.scarango.response.meta.collection

/**
 * Collection result with human readable `type` codes
 */
trait EnumTypes {
  val `type`: Int

  def enumType: Type = Types.byValue(`type`)
}
