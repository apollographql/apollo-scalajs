package com.apollographql.scalajs

import com.apollographql.scalajs.link.{ApolloLink, GraphQLRequest, HttpLink, HttpLinkOptions}
import org.scalatest.{Assertion, AsyncFunSuite}

import scala.concurrent.Promise
import scala.scalajs.js

class ApolloLinkTest extends AsyncFunSuite {
  js.Dynamic.global.window.fetch = UnfetchFetch

  implicit override def executionContext =
    scala.concurrent.ExecutionContext.Implicits.global

  test("Can perform a query with an HttpLink") {
    val resultPromise = Promise[Assertion]
    ApolloLink.execute(
      new HttpLink(HttpLinkOptions("https://graphql-currency-rates.glitch.me")),
      GraphQLRequest(
        gql(
          """{
            |  rates(currency: "USD") {
            |    currency
            |  }
            |}""".stripMargin
        )
      )
    ).forEach { res =>
      resultPromise.success(assert(true))
    }

    resultPromise.future
  }
}
