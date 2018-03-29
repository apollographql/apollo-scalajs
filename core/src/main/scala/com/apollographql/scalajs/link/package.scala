package com.apollographql.scalajs

import scala.scalajs.js
import scala.scalajs.js.|

package object link {
  type Subscriber[T] = js.Function1[SubscriptionObserver[T], Unit | js.Function0[Unit] | Subscription]

  type NextLink = js.Function1[Operation, Observable[FetchResult]]
  type RequestHandler = js.Function2[Operation, js.UndefOr[NextLink], Observable[FetchResult]]
}
