package com.auginte.scarango.request

import com.auginte.scarango.state.{DatabaseNames, CollectionName, DatabaseName}
import spray.http.Uri
import spray.json.JsValue

/**
 * Operation to create new document
 */
case class CreateDocument(document: String, createCollection: Boolean = false, waitForSync: Boolean = true)(implicit val collection: CollectionName, implicit val database: DatabaseName = DatabaseNames.default)
  extends CreateRequest with groups.Collection {
  override protected def toJson: JsValue = throw new RuntimeException("Should be overwritten in later call")

  override protected def entityData: String = document

  override val uri: Uri = Uri(s"/_db/$database/_api/document?collection=$collection&createCollection=$createCollection&waitForSync=$waitForSync")
}
