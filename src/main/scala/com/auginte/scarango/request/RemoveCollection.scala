package com.auginte.scarango.request

import com.auginte.scarango.request.parts.Authorisation
import com.auginte.scarango.state.{DatabaseNames, CollectionName, DatabaseName}
import spray.http.Uri

case class RemoveCollection(name: CollectionName)
                           (implicit val database: DatabaseName = DatabaseNames.default,
                            override implicit val authorisation: Authorisation = Authorisation.default)
  extends RemoveRequest with groups.Collection {


  override lazy val uri: Uri = Uri(s"/_db/$database/_api/collection/$name")
}
