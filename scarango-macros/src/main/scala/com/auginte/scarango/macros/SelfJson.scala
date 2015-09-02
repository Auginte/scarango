package com.auginte.scarango.macros

import scala.language.experimental.macros
import scala.reflect.macros.whitebox
import spray.json._

/**
 * Generates Spray Json formatter for provided class
 *
 * So all Json conversion can be in same class
 */
object SelfJson {

  /**
   *Macro thart generates:
   * {{{
   *   JsObject(
   *     "name" -> name.toJson
   *   )
   * }}}
   *
   * Do not forget to:
   * {{{
   *   import spray.json._
   *   import spray.json.DefaultJsonProtocol._
   * }}}
   *
   * @tparam T Case class to take fields from
   * @return mapper to convert to json
   */
  def materialise[T]: JsValue = macro materializeJsonImpl[T]

  def materializeJsonImpl[T: c.WeakTypeTag](c: whitebox.Context): c.Expr[JsValue] = {
    import c.universe._

    val weakType = weakTypeOf[T]
    val fields = weakType.decls.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor => m
    }.get.paramLists.head

    val toJson = fields.map { field =>
      val name = field.name.toTermName
      val decoded = name.decodedName.toString

      q"$decoded -> $name.toJson"
    }
    c.Expr[JsValue] {q"JsObject(Map(..$toJson))"}
  }
}
