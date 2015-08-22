package com.auginte.scarango.response

import spray.http.HttpResponse

case class RawResponse(raw: HttpResponse) extends ResponseData
