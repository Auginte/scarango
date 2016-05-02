package com.auginte.scarango.errors

import akka.http.scaladsl.model.HttpResponse
import com.auginte.scarango.Context

/**
  * Exception, when resource is not found: 404 response
  */
case class NotFound(httpResponse: HttpResponse)(implicit context: Context) extends ScarangoException("Not found")(context)
