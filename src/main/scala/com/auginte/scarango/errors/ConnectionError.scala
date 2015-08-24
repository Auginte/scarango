package com.auginte.scarango.errors

import com.auginte.scarango.request.Request

/**
 * Not recognised lower level ArangoDB response. For example TCP connection closed
 */
case class ConnectionError(raw: Any, lastRequest: Request)
  extends ScarangoError("ArangoDb connection problem. Request unprocessed.")
  with UnprocessedRequest