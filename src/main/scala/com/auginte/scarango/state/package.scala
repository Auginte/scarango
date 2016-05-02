package com.auginte.scarango

package object state {
  def database(implicit context: Context) = new Connection(context).connectionFlow
}
