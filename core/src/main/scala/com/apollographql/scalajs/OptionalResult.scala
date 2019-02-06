package com.apollographql.scalajs

import scala.scalajs.js
import scala.language.implicitConversions

@js.native trait OptionalValue[T] extends js.Object
object OptionalValue {
  implicit class OptionalValueExt[T](private val optionalValue: OptionalValue[T]) extends AnyVal {
    def toOption: Option[T] = Option(optionalValue.asInstanceOf[T])
  }

  final def empty[T]: OptionalValue[T] = null.asInstanceOf[OptionalValue[T]]
  implicit def toOption[T](v: OptionalValue[T]): Option[T] = v.toOption
  implicit def fromValue[T](v: T): OptionalValue[T] = v.asInstanceOf[OptionalValue[T]]
  implicit def fromOption[T](v: Option[T])(implicit ev: Null <:< T): OptionalValue[T] = v.orNull.asInstanceOf[OptionalValue[T]]
}
