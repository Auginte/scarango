package com.auginte.scarango.remove

import spray.http.Uri

case class Database(name: String) extends Request {
  override lazy val uri: Uri = Uri(s"/_api/database/$name")

  override def toString: String = s"Remove.Database($name)"
}
