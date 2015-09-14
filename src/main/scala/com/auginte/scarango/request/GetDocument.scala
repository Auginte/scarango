package com.auginte.scarango.request

import com.auginte.scarango.request.parts.Authorisation
import com.auginte.scarango.state.{DatabaseNames, DatabaseName}
import spray.http.Uri

case class GetDocument(id: String)
                      (implicit val database: DatabaseName = DatabaseNames.default.toString,
                       override implicit val authorisation: Authorisation = Authorisation.default)
  extends GetRequest with groups.Document {


  override val uri: Uri = Uri(s"/_db/$database/_api/document/$id")
}
