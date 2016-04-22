package com.auginte.scarango

import akka.http.scaladsl.model.{HttpMethods, HttpProtocols, HttpRequest}

/**
  * Named request queries
  */
package object request {
  private def headers(context: Context) = List(context.authorisation.header)
  private val get = HttpMethods.GET

  def getVersion(implicit context: Context) = HttpRequest(get, "/_api/version", headers(context))
}
