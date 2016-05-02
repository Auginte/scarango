package com.auginte.scarango.errors

import akka.http.scaladsl.model.HttpResponse

/**
  * Marker for errors with response context
  */
trait ResponseData {
  val httpResponse: HttpResponse
}
