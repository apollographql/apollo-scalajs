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
  @js.native
  trait ApolloClientInstanceRawInterface extends js.Object {
    def query(options: ObjectOrWritten[QueryOptions]): js.Promise[js.Object] = js.native
  }

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
