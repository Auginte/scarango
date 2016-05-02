package com.auginte.scarango.errors

import akka.http.scaladsl.model.HttpResponse
import com.auginte.scarango.Context

case class Unexpected(cause: Exception, httpResponse: HttpResponse)(implicit context: Context)
  extends ScarangoException("Unexpected Scarango exception: " + Unexpected.message(cause), cause)(context)

object Unexpected {
  private def message(throwable: Throwable) = if (throwable != null) throwable.getMessage else "[cause=null]"
}
