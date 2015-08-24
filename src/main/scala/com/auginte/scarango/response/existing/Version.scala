package com.auginte.scarango.response.existing

import com.auginte.scarango.response.ResponseData

case class Version(version: String = "0.0.0", server: String = "none") extends ResponseData
