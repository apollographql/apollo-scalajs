package com.apollographql.scalajs

import com.apollographql.scalajs.cache.InMemoryCache
import com.apollographql.scalajs.link.{HttpLink, HttpLinkOptions}
import org.scalatest.AsyncFunSuite

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("unfetch", JSImport.Default)
object UnfetchFetch extends js.Object

class ApolloClientTest extends AsyncFunSuite {
  js.Dynamic.global.window.fetch = UnfetchFetch

  implicit override def executionContext =
    scala.concurrent.ExecutionContext.Implicits.global

  test("Can create an instance of Apollo Client") {
    assert(!js.isUndefined(
      new ApolloClient(
        ApolloClientOptions(
          uri = "https://graphql-currency-rates.glitch.me",
          cache = new InMemoryCache()
        )
      )
    ))
  }

  test("Can perform a simple query and get the results") {
    new ApolloClient(
        ApolloClientOptions(
          uri = "https://graphql-currency-rates.glitch.me",
          cache = new InMemoryCache()
        )
    ).query[js.Object](
      query = gql(
        """{
          |  rates(currency: "USD") {
          |    currency
          |  }
          |}""".stripMargin
      )
    ).map { r =>
      assert(r.data.asInstanceOf[js.Dynamic]
        .rates.asInstanceOf[js.Array[js.Object]].length > 0)
    }
  }

  test("Can perform a query with variables and get the results") {
    new ApolloClient(
        ApolloClientOptions(
          uri = "https://graphql-currency-rates.glitch.me",
          cache = new InMemoryCache()
        )
    ).query[js.Object, js.Object](
      query = gql(
        """query GetRates($cur: String!) {
          |  rates(currency: $cur) {
          |    currency
          |  }
          |}""".stripMargin
      ),
      variables = js.Dynamic.literal(
        cur = "USD"
      )
    ).map { r =>
      assert(r.data.asInstanceOf[js.Dynamic]
        .rates.asInstanceOf[js.Array[js.Object]].length > 0)
    }
  }

  test("Can perform a query with typed variables and response and get the results") {
    case class Variables(cur: String)
    case class Rate(currency: String)
    case class QueryResult(rates: Seq[Rate])

    new ApolloClient(
        ApolloClientOptions(
          uri = "https://graphql-currency-rates.glitch.me",
          cache = new InMemoryCache()
        )
    ).query[QueryResult, Variables](
      query = gql(
        """query GetRates($cur: String!) {
          |  rates(currency: $cur) {
          |    currency
          |  }
          |}""".stripMargin
      ),
      variables = Variables(
        cur = "USD"
      )
    ).map { r =>
      assert(r.data.rates.nonEmpty)
    }
  }
}
