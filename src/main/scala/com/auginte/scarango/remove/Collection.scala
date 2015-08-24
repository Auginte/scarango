package com.auginte.scarango.remove

import spray.http.Uri

case class Collection(name: String) extends Request {
  override lazy val uri: Uri = Uri(s"/_api/collection/$name")

  override def toString: String = s"Remove.Collection($name)"
}
