package com.auginte.scarango.request

import spray.http.Uri
import spray.json.JsValue

/**
 * Operation to create new document
 */
case class CreateDocument(collection: String, document: String, createCollection: Boolean = false, waitForSync: Boolean = true)
  extends CreateRequest with groups.Collection {
  override protected def toJson: JsValue = throw new RuntimeException("Should be overwritten in later call")

  override protected def entityData: String = document

  override val uri: Uri = Uri(s"/_api/document?collection=$collection&createCollection=$createCollection&waitForSync=$waitForSync")
}
