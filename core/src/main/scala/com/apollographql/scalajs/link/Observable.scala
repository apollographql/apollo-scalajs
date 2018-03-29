package com.apollographql.scalajs.link

import slinky.readwrite.ObjectOrWritten

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
trait SubscriptionObserver[T] extends js.Object {
  var closed: Boolean = js.native
  def next(value: T): Unit = js.native
  def error(errorValue: js.Any): Unit = js.native
  def complete(): Unit = js.native
}

@js.native
trait Subscription extends js.Object {
  var closed: Boolean = js.native
  def unsubscribe(): Unit = js.native
}

@JSImport("apollo-link", "Observable")
@js.native
class Observable[T](subscriber: Subscriber[T]) extends js.Object {
  def subscribe(observerOrNext: ObjectOrWritten[T => Unit],
                error: ObjectOrWritten[js.UndefOr[js.Any => Unit]] = js.undefined,
                complete: ObjectOrWritten[js.UndefOr[() => Unit]] = js.undefined): Subscription = js.native
  def subscribe(observer: SubscriptionObserver[T]): Subscription = js.native

  def forEach(fn: js.Function1[T, Unit]): js.Promise[Unit] = js.native
}
