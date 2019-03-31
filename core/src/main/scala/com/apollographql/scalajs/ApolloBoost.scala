package com.apollographql.scalajs

import com.apollographql.scalajs.link.Operation
import slinky.readwrite.ObjectOrWritten

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

case class ApolloBoostClientOptions(uri: String,
                                    fetchOptions: js.UndefOr[js.Object] = js.undefined,
                                    request: js.UndefOr[js.Function1[Operation, js.Promise[js.Any]]] = js.undefined,
                                    onError: js.UndefOr[js.Function1[ApolloError, js.Any]] = js.undefined)

@js.native
trait ApolloError extends js.Object {
  val operation: js.Object = js.native
  val response: js.Object = js.native
  val graphQLErrors: js.Object = js.native
  val networkError: ApolloNetworkError = js.native
}

@js.native
trait ApolloNetworkError extends js.Object {
  val name: String = js.native
  val response: js.Object = js.native
  val statusCode: Int = js.native
  val bodyText: String = js.native
}

@JSImport("apollo-boost", JSImport.Default)
@js.native
class ApolloBoostClient(options: ObjectOrWritten[ApolloBoostClientOptions]) extends ApolloClientInstance
object ApolloBoostClient {
  def apply(uri: String) = new ApolloBoostClient(ApolloBoostClientOptions(uri = uri))
}
