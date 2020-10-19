package com.apollographql.scalajs.link

import com.apollographql.scalajs.DocumentNode

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@js.native
trait GraphQLRequest extends js.Object {
  val query: DocumentNode = js.native
}

object GraphQLRequest {
  def apply(query: DocumentNode): GraphQLRequest = {
    js.Dynamic.literal(
      query = query
    ).asInstanceOf[GraphQLRequest]
  }
}

@js.native
trait FetchResult extends js.Object

@js.native
trait Operation extends js.Object {
  def setContext(context: js.Dictionary[js.Any]): js.Dictionary[js.Any] = js.native
  def getContext(): js.Dictionary[js.Any] = js.native
  def toKey(): String = js.native
}

@JSImport("@apollo/client", "ApolloLink")
@js.native
class ApolloLink(handler: js.UndefOr[js.Function2[Operation, js.UndefOr[NextLink], Observable[FetchResult]]] = js.undefined) extends js.Object {
  def concat(next: ApolloLink | RequestHandler): ApolloLink = js.native
  def request(operation: Operation, forward: js.UndefOr[NextLink]): Observable[FetchResult] = js.native
}

@JSImport("@apollo/client", "ApolloLink")
@js.native
object ApolloLink extends js.Object {
  def empty(): ApolloLink = js.native
  def from(links: js.Array[ApolloLink]): ApolloLink = js.native
  def concat(first: ApolloLink, second: ApolloLink): ApolloLink = js.native
  def execute(link: ApolloLink, operation: GraphQLRequest): Observable[FetchResult] = js.native
}
