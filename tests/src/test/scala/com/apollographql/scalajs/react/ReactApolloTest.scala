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

  test("Can render data to string based on a query") {
    js.Dynamic.global.fetch = UnfetchFetch

    val gotDataPromise = Promise[Assertion]
    val gotRenderedStringPromise = Promise[Assertion]
    val link = new HttpLink(options = HttpLinkOptions(uri = "https://w5xlvm3vzz.lp.gql.zone/graphql"))
    val cache = new InMemoryCache()
    val client = new ApolloClient(options = js.Dynamic.literal(ssrMode = true, link = link, cache = cache))

    ReactApolloServer.renderToStringWithData(
      ApolloProvider(ApolloProvider.Props(client = client))(
        Query(
          CurrencyRatesQuery,
          CurrencyRatesQuery.Variables("USD"),
          queryOptions = ExtraQueryOptions(ssr = true)) { d =>
          if (d.data.isDefined) {
            gotDataPromise.success(assert(d.data.get.rates.exists(_.nonEmpty)))
            println(JSON.stringify(d.data.get.rates.head))
            div(JSON.stringify(d.data.get.rates.head))
          } else ""
        }
      )
    ).toFuture.foreach(html => {
      gotRenderedStringPromise.success(assert(html.length > 0))
      html
    })
    gotRenderedStringPromise.future
    gotDataPromise.future
  }
}
