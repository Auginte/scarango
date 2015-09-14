package com.auginte.scarango.request

import com.auginte.scarango.request.parts.Authorisation
import com.auginte.scarango.state.{DatabaseNames, CollectionName, DatabaseName}
import spray.http.Uri

case class GetCollection(name: CollectionName)
                        (implicit val database: DatabaseName = DatabaseNames.default,
                         override implicit val authorisation: Authorisation = Authorisation.default)
  extends GetRequest with groups.Collection {


  override val uri: Uri = Uri(s"/_db/$database/_api/collection/$name")
}
