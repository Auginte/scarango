package com.auginte.scarango.response.raw

/**
 * Raw response from CreateDocument
 */
case class RawDocumentCreated(error: Boolean, _id: String, _rev: String, _key: String)