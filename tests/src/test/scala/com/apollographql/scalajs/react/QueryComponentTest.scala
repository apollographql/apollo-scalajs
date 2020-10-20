package com.apollographql.scalajs.react

import com.apollographql.scalajs._
import org.scalajs.dom.document
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.Assertion
import slinky.web.ReactDOM
import slinky.web.html.div

import scala.concurrent.Promise
import scala.scalajs.js
import com.apollographql.scalajs.cache.InMemoryCache

class QueryComponentTest extends AsyncFunSuite {
  js.Dynamic.global.window.fetch = UnfetchFetch

  implicit override def executionContext =
    scala.concurrent.ExecutionContext.Implicits.global

  test("Can mount a Query component and render data based on the query") {
    val gotDataPromise = Promise[Assertion]
    case class ResultShape(rates: Seq[js.Object])
    ReactDOM.render(
      ApolloProvider(
        client = new ApolloClient(
            ApolloClientOptions(
              uri = "https://graphql-currency-rates.glitch.me",
              cache = new InMemoryCache()
            )
          )
      )(
        Query[ResultShape](gql(
          """{
            |  rates(currency: "USD") {
            |    currency
            |  }
            |}""".stripMargin
        )) { d =>
          if (d.data.isDefined) {
            gotDataPromise.success(assert(d.data.get.rates.nonEmpty))
          }

          div()
        }
      ),
      document.createElement("div")
    )

    gotDataPromise.future
  }

  test("Can mount a Query component that takes variables and render data based on the query") {
    val gotDataPromise = Promise[Assertion]
    case class ResultShape(rates: Seq[js.Object])
    case class Variables(cur: String)
    ReactDOM.render(
      ApolloProvider(
        client = new ApolloClient(
            ApolloClientOptions(
              uri = "https://graphql-currency-rates.glitch.me",
              cache = new InMemoryCache()
            )
          )
      )(
        Query[ResultShape, Variables](gql(
          """query GetRates($cur: String!) {
            |  rates(currency: $cur) {
            |    currency
            |  }
            |}""".stripMargin
        ), Variables("USD")) { d =>
          if (d.data.isDefined) {
            gotDataPromise.success(assert(d.data.get.rates.nonEmpty))
          }

          div()
        }
      ),
      document.createElement("div")
    )

    gotDataPromise.future
  }

  test("Can mount a Query component and render data based a query returning multiple values") {
    val gotDataPromise = Promise[Assertion]
    case class ResultShape(rates: Seq[js.Object], bar: Seq[js.Object])
    ReactDOM.render(
      ApolloProvider(
        client = new ApolloClient(
            ApolloClientOptions(
              uri = "https://graphql-currency-rates.glitch.me",
              cache = new InMemoryCache()
            )
          )
      )(
        Query[ResultShape](gql(
          """{
            |  rates(currency: "USD") {
            |    currency
            |  }
            |
            |  bar: rates(currency: "USD") {
            |    currency
            |  }
            |}""".stripMargin
        )) { d =>
          if (d.data.isDefined) {
            gotDataPromise.success(assert(d.data.get.rates.nonEmpty && d.data.get.bar.nonEmpty))
          }

          div()
        }
      ),
      document.createElement("div")
    )

    gotDataPromise.future
  }

  test("Can mount a Query component and render data based a static query") {
    val gotDataPromise = Promise[Assertion]
    ReactDOM.render(
      ApolloProvider(
        client = new ApolloClient(
            ApolloClientOptions(
              uri = "https://graphql-currency-rates.glitch.me",
              cache = new InMemoryCache()
            )
          )
      )(
        Query(UsdRatesQuery) { d =>
          if (d.data.isDefined) {
            gotDataPromise.success(assert(d.data.get.rates.exists(_.nonEmpty)))
          }

          div()
        }
      ),
      document.createElement("div")
    )

    gotDataPromise.future
  }

  test("Can mount a Query component and render data based a static query with variables") {
    val gotDataPromise = Promise[Assertion]
    ReactDOM.render(
      ApolloProvider(
        client = new ApolloClient(
            ApolloClientOptions(
              uri = "https://graphql-currency-rates.glitch.me",
              cache = new InMemoryCache()
            )
          )
      )(
        Query(CurrencyRatesQuery, CurrencyRatesQuery.Variables("USD")) { d =>
          if (d.data.isDefined) {
            gotDataPromise.success(assert(d.data.get.rates.exists(_.nonEmpty)))
          }

          div()
        }
      ),
      document.createElement("div")
    )

    gotDataPromise.future
  }

  test("Can mount a Query component with a cache-only fetch policy") {
    val didntLoadPromise = Promise[Assertion]
    ReactDOM.render(
      ApolloProvider(
        client = new ApolloClient(
            ApolloClientOptions(
              uri = "https://this-does-not-exists.com",
              cache = new InMemoryCache()
            )
          )
      )(
        Query(CurrencyRatesQuery, CurrencyRatesQuery.Variables("USD"), ExtraQueryOptions(
          fetchPolicy = "cache-only"
        )) { d =>
          // TODO: Check if this is the intended behavior
          if (!d.loading && d.error.isEmpty) {
            didntLoadPromise.trySuccess(assert(true))
          }

          div()
        }
      ),
      document.createElement("div")
    )

    didntLoadPromise.future
  }
}
