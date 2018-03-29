package com.apollographql.scalajs.cache

import slinky.readwrite.ObjectOrWritten

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
trait CacheResolverContext extends js.Object {
  def getCacheKey(value: js.Object): js.Object = js.native
}

case class InMemoryCacheOptions(cacheRedirects: js.UndefOr[js.Dictionary[js.Dictionary[CacheResolver]]] = js.undefined)

@JSImport("apollo-cache-inmemory", "InMemoryCache")
@js.native
class InMemoryCache(options: js.UndefOr[ObjectOrWritten[InMemoryCacheOptions]] = js.undefined) extends ApolloCache
