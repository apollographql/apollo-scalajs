package com.apollographql.scalajs.cache

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
trait CacheResolverContext extends js.Object {
  def getCacheKey(value: js.Object): js.Object = js.native
}

trait InMemoryCacheConfig {
  val resultCaching: js.UndefOr[Boolean]
  val possibleTypes: js.UndefOr[js.Dynamic]
  val typePolicies: js.UndefOr[js.Dynamic]
}

object InMemoryCacheConfig {
  def apply(
    resultCaching: js.UndefOr[Boolean] = js.undefined,
    possibleTypes: js.UndefOr[js.Dynamic] = js.undefined,
    typePolicies: js.UndefOr[js.Dynamic] = js.undefined
  ): InMemoryCacheConfig = {
    js.Dynamic.literal(
      resultCaching = resultCaching,
      possibleTypes = possibleTypes,
      typePolicies = typePolicies
    ).asInstanceOf[InMemoryCacheConfig]
  }
}

@JSImport("@apollo/client/cache", "InMemoryCache")
@js.native
class InMemoryCache(options: js.UndefOr[InMemoryCacheConfig] = js.undefined) extends ApolloCache
