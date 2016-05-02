package com.auginte.scarango.errors

import akka.http.scaladsl.model.HttpResponse
import com.auginte.scarango.Context

/**
  * Exception, when there are user permission problems: 403 response
  */
case class Forbidden(httpResponse: HttpResponse)(implicit context: Context) extends ScarangoException("Forbidden")(context)
