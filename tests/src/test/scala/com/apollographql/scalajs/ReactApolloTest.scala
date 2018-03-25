package com.apollographql.scalajs

import org.scalatest.{Assertion, AsyncFunSuite}
import slinky.web.ReactDOM

import scala.scalajs.js
import org.scalajs.dom.document
import slinky.web.html.div

import scala.concurrent.{Future, Promise}

class ReactApolloTest extends AsyncFunSuite {
  js.Dynamic.global.fetch = UnfetchFetch

  implicit override def executionContext =
    scala.concurrent.ExecutionContext.Implicits.global

  test("Can mount an ApolloProvider with a client instance") {
    assert(!js.isUndefined(
      ReactDOM.render(
        ApolloProvider(
          client = ApolloBoostClient(uri = "https://w5xlvm3vzz.lp.gql.zone/graphql")
        )(
          div()
        ),
        document.createElement("div")
      )
    ))
  }
}
