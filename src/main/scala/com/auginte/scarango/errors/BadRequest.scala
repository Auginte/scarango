package com.auginte.scarango.errors

import akka.http.scaladsl.model.HttpResponse
import com.auginte.scarango.Context

/**
  * Exception, when endpoint isn ot found: 400 response
  */
case class BadRequest(httpResponse: HttpResponse)(implicit context: Context) extends ScarangoException("Bad request")(context)
