package com.auginte.scarango.common

import java.text.SimpleDateFormat
import java.util.Calendar

import scala.util.Random

/**
 * Small function for easier testing of the driver
 */
object TestKit {
  def date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime)

  def unique = "test_" + date + "_" + Random.nextInt(999)
}
