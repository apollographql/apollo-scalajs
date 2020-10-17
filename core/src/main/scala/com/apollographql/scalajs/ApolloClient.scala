package com.apollographql.scalajs

import slinky.readwrite.{ObjectOrWritten, Reader}

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

case class ApolloClientOptions(
  uri: String, 
  link: js.UndefOr[js.Object] = js.undefined, 
  cache: js.Object, 
  ssrMode: Boolean = false
)

@JSImport("@apollo/client", "ApolloClient")
@js.native
class ApolloClient(options: ObjectOrWritten[ApolloClientOptions]) extends ApolloClientInstance

case class QueryOptions(query: DocumentNode, variables: js.UndefOr[js.Object] = js.undefined)

case class QueryResult[T](data: T)
object QueryResult {
  implicit def reader[T](implicit tReader: Reader[T]): Reader[QueryResult[T]] = { o =>
    val dyn = o.asInstanceOf[js.Dynamic]
    QueryResult[T](
      tReader.read(dyn.data.asInstanceOf[js.Object])
    )
  }
}

@js.native
trait ApolloClientInstance extends js.Object
object ApolloClientInstance {
  @js.native
  private trait ApolloClientInstanceRawInterface extends js.Object {
    def query(options: ObjectOrWritten[QueryOptions]): js.Promise[js.Object] = js.native
  }

  implicit class RichInstance(val i: ApolloClientInstance) extends AnyVal {
    private def raw = i.asInstanceOf[ApolloClientInstanceRawInterface]
    def query[R](options: QueryOptions)(implicit ec: ExecutionContext, rReader: Reader[R]): Future[QueryResult[R]] = {
      raw.query(options).toFuture.map(QueryResult.reader(rReader).read)
    }

    def query[R](query: DocumentNode)(implicit ec: ExecutionContext, rReader: Reader[R]): Future[QueryResult[R]] = {
      raw.query(QueryOptions(query)).toFuture.map(QueryResult.reader(rReader).read)
    }

    def query[R, V](query: DocumentNode, variables: ObjectOrWritten[V])
                   (implicit ec: ExecutionContext, rReader: Reader[R]): Future[QueryResult[R]] = {
      raw.query(QueryOptions(query, variables)).toFuture.map(QueryResult.reader(rReader).read)
    }
  }
}
