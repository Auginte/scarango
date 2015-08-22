package com.auginte.scarango.errors

import akka.io.Tcp.ErrorClosed
import com.auginte.scarango.get.Request

case class DatabaseClosed(raw: ErrorClosed, lastRequest: Request)
  extends ScarangoError("ArangoDb connection closed unexpectedly")
  with UnprocessedRequest