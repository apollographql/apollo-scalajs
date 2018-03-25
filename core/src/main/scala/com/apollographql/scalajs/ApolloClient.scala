package com.apollographql.scalajs

import slinky.readwrite.{ObjectOrWritten, Reader}

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

case class QueryOptions(query: ParsedQuery, variables: js.UndefOr[js.Object] = js.undefined)

case class QueryResult(data: js.Object)

@js.native
trait ApolloClientInstance extends js.Object
object ApolloClientInstance {
  implicit class RichInstance(val i: ApolloClientInstance) extends AnyVal {
    private def raw = i.asInstanceOf[ApolloClientInstanceRawInterface]
    def query(options: QueryOptions)(implicit ec: ExecutionContext): Future[QueryResult] = {
      raw.query(options).toFuture.map(implicitly[Reader[QueryResult]].read)
    }

    def query(query: ParsedQuery)(implicit ec: ExecutionContext): Future[QueryResult] = {
      raw.query(QueryOptions(query)).toFuture.map(implicitly[Reader[QueryResult]].read)
    }

    def query(query: ParsedQuery, variables: js.Object)(implicit ec: ExecutionContext): Future[QueryResult] = {
      raw.query(QueryOptions(query, variables)).toFuture.map(implicitly[Reader[QueryResult]].read)
    }
  }
}

@js.native
trait ApolloClientInstanceRawInterface extends js.Object {
  def query(options: ObjectOrWritten[QueryOptions]): js.Promise[js.Object] = js.native
}

case class ApolloBoostClientOptions(uri: String,
                                    fetchOptions: js.UndefOr[js.Object] = js.undefined)

@JSImport("apollo-boost", JSImport.Default)
@js.native
class ApolloBoostClient(options: ObjectOrWritten[ApolloBoostClientOptions]) extends ApolloClientInstance
object ApolloBoostClient {
  def apply(uri: String) = new ApolloBoostClient(ApolloBoostClientOptions(uri = uri))
}

@js.native
trait ParsedQuery extends js.Object

@js.native
@JSImport("graphql-tag", JSImport.Default)
object gql extends js.Object {
  def apply(query: String): ParsedQuery = js.native
}

@js.native
@JSImport("react-apollo", JSImport.Namespace)
object ReactApollo extends js.Object {
  val ApolloProvider: js.Object = js.native
  val Query: js.Object = js.native
  val Mutation: js.Object = js.native
}
