package com.apollographql.scalajs.react

import com.apollographql.scalajs.cache.InMemoryCache
import com.apollographql.scalajs.link.{HttpLink, HttpLinkOptions}
import com.apollographql.scalajs.{ApolloClient, CurrencyRatesQuery, UnfetchFetch}
import org.scalajs.dom.document
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.Assertion
import slinky.web.ReactDOM
import slinky.web.html.div

import scala.concurrent.Promise
import scala.scalajs.js
import scala.scalajs.js.JSON
import com.apollographql.scalajs.ApolloClientOptions

class ReactApolloTest extends AsyncFunSuite {
  js.Dynamic.global.window.fetch = UnfetchFetch

  implicit override def executionContext =
    scala.concurrent.ExecutionContext.Implicits.global

  test("Can mount an ApolloProvider with a client instance") {
    assert(!js.isUndefined(
      ReactDOM.render(
        ApolloProvider(
          client = new ApolloClient(
            ApolloClientOptions(
              uri = "https://graphql-currency-rates.glitch.me",
              cache = new InMemoryCache()
            )
          )
        )(
          div()
        ),
        document.createElement("div")
      )
    ))
  }

  test("Can server-side render data to string based on a query") {
    val link = new HttpLink(options = HttpLinkOptions(uri = "https://graphql-currency-rates.glitch.me"))
    val cache = new InMemoryCache()
    val client = new ApolloClient(options = ApolloClientOptions(ssrMode = true, link = link, cache = cache))

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
      assert(html == """<div>AED</div>""")
    }
  }
}
