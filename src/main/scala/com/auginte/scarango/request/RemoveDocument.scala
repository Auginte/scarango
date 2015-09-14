package com.auginte.scarango.request

import com.auginte.scarango.request.parts.Authorisation
import com.auginte.scarango.state.{DatabaseNames, DatabaseName}
import spray.http.Uri

case class RemoveDocument(id: String)
                         (implicit val database: DatabaseName = DatabaseNames.default,
                          override implicit val authorisation: Authorisation = Authorisation.default)
  extends RemoveRequest with groups.Document {


  override lazy val uri: Uri = Uri(s"/_db/$database/_api/document/$id")
}
