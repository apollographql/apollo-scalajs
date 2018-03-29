package com.apollographql.scalajs

import scala.scalajs.js

package object cache {
  type CacheResolver = js.Function3[js.Any, js.Dictionary[js.Any], CacheResolverContext, js.Any]
}
