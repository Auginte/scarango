package com.auginte.scarango.response

import com.auginte.scarango.response.common.CommonResponse
import com.auginte.scarango.state.DatabaseName

case class DatabaseList(result: List[DatabaseName], error: Boolean, code: Int) extends Data with CommonResponse
