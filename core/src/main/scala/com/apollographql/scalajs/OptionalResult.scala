package com.apollographql.scalajs

import scala.scalajs.js
import scala.language.implicitConversions

@js.native trait OptionalResult[T] extends js.Object
object OptionalResult {
  implicit def toOption[T](orig: OptionalResult[T]): Option[T] = Option(orig.asInstanceOf[T])
}
