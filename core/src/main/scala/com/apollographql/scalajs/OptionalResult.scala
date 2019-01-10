package com.apollographql.scalajs

import scala.scalajs.js
import scala.language.implicitConversions

@js.native trait OptionalInput[T] extends js.Object
object OptionalInput {
  final def empty[T]: OptionalInput[T] = null.asInstanceOf[OptionalInput[T]]
  implicit def fromValue[T](v: T): OptionalInput[T] = v.asInstanceOf[OptionalInput[T]]
  implicit def fromOption[T](v: Option[T])(implicit ev: Null <:< T): OptionalInput[T] = v.orNull.asInstanceOf[OptionalInput[T]]
}

@js.native trait OptionalResult[T] extends js.Object
object OptionalResult {
  implicit def toOption[T](orig: OptionalResult[T]): Option[T] = Option(orig.asInstanceOf[T])
}
