package com.apollographql.scalajs

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
trait ApolloClientOptions extends js.Object {
  val uri: js.UndefOr[String] = js.native
  val link: js.UndefOr[js.Object] = js.native
  val cache: js.UndefOr[js.Object] = js.native
  val name: js.UndefOr[String] = js.native
  val version: js.UndefOr[String] = js.native
  val ssrMode: js.UndefOr[Boolean] = js.native
  val ssrForceFetchDelay: js.UndefOr[Int] = js.native
  val connectToDevTools: js.UndefOr[Boolean] = js.native
  val queryDeduplication: js.UndefOr[Boolean] = js.native
  val defaultOptions: js.UndefOr[js.Dynamic] = js.native
}

object ApolloClientOptions {
  def apply(
    uri: js.UndefOr[String] = js.undefined,
    link: js.UndefOr[js.Object] = js.undefined,
    cache: js.UndefOr[js.Object] = js.undefined,
    name: js.UndefOr[String] = js.undefined,
    version: js.UndefOr[String] = js.undefined,
    ssrMode: js.UndefOr[Boolean] = js.undefined,
    ssrForceFetchDelay: js.UndefOr[Int] = js.undefined,
    connectToDevTools: js.UndefOr[Boolean] = js.undefined,
    queryDeduplication: js.UndefOr[Boolean] = js.undefined,
    defaultOptions: js.UndefOr[js.Dynamic] = js.undefined
  ): ApolloClientOptions = {
    js.Dynamic.literal(
      uri = uri,
      link = link,
      cache = cache,
      name = name,
      version = version,
      ssrMode = ssrMode,
      ssrForceFetchDelay = ssrForceFetchDelay,
      connectToDevTools = connectToDevTools,
      queryDeduplication = queryDeduplication,
      defaultOptions = defaultOptions
    ).asInstanceOf[ApolloClientOptions]
  }
}

@JSImport("@apollo/client", "ApolloClient")
@js.native
class ApolloClient(options: ApolloClientOptions) extends js.Object {
  def query[D <: js.Object, V <: js.Any](options: QueryOptions[V]): js.Promise[ApolloQueryResult[D]] = js.native
}

@js.native
trait QueryOptions[V] extends js.Object {
  val query: DocumentNode = js.native
  val variables: js.UndefOr[V] = js.native
}

object QueryOptions {
  def apply[V](
    query: DocumentNode,
    variables: js.UndefOr[V] = js.undefined
  ) = {
    js.Dynamic.literal(
      query = query,
      variables = variables.asInstanceOf[js.Object]
    ).asInstanceOf[QueryOptions[V]]
  }
}

@js.native
trait ApolloQueryResult[D] extends js.Object {
  val data: js.UndefOr[D]
  val errors: js.UndefOr[js.Array[js.Dynamic]]
  val error: js.UndefOr[js.Dynamic]
  val loading: Boolean
  val networkStatus: Int
  val partial: js.UndefOr[Boolean]
}
