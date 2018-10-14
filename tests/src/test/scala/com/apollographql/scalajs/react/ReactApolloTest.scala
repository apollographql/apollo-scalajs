package com.apollographql.scalajs.react

import com.apollographql.scalajs.cache.InMemoryCache
import com.apollographql.scalajs.link.{HttpLink, HttpLinkOptions}
import com.apollographql.scalajs.{ApolloBoostClient, ApolloClient, CurrencyRatesQuery, UnfetchFetch}
import org.scalajs.dom.document
import org.scalatest.{Assertion, AsyncFunSuite}
import slinky.web.ReactDOM
import slinky.web.html.div

import scala.concurrent.Promise
import scala.scalajs.js
import scala.scalajs.js.JSON

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

  test("Can server-side render data to string based on a query") {
    js.Dynamic.global.fetch = UnfetchFetch

    val link = new HttpLink(options = HttpLinkOptions(uri = "https://w5xlvm3vzz.lp.gql.zone/graphql"))
    val cache = new InMemoryCache()
    val client = new ApolloClient(options = js.Dynamic.literal(ssrMode = true, link = link, cache = cache))

    ReactApolloServer.renderToStringWithData(
      ApolloProvider(ApolloProvider.Props(client = client))(
        Query(
          CurrencyRatesQuery,
          CurrencyRatesQuery.Variables("USD")
        ) { d =>
          if (d.data.isDefined) {
            div(d.data.get.rates.get.head.get.currency.get)
          } else ""
        }
      )
    ).toFuture.map { html =>
      assert(html == """<div data-reactroot="">AED</div>""")
    }
  }
}
