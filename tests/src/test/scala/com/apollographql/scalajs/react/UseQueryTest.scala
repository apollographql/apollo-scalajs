package com.apollographql.scalajs.react

import com.apollographql.scalajs._
import org.scalajs.dom.document
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.Assertion
import slinky.web.ReactDOM
import slinky.web.html.div
import slinky.core.annotations.react
import slinky.core.facade.Hooks._

import scala.concurrent.Promise
import scala.scalajs.js
import com.apollographql.scalajs.cache.InMemoryCache
import com.apollographql.scalajs.react.HooksApi._
import slinky.core.FunctionalComponent

class UseQueryTest extends AsyncFunSuite {
  js.Dynamic.global.window.fetch = UnfetchFetch

  implicit override def executionContext =
    scala.concurrent.ExecutionContext.Implicits.global

  test("Can make a simple query") {
    val gotDataPromise = Promise[Assertion]

    @react object UseQueryTestComponent {
      case class Props()

      val component = FunctionalComponent[Props] { _ =>
        val QueryResult(data, _, _) = useQuery[UsdRatesQuery.type](UsdRatesQuery.operation)

        useEffect(() => {
          if (data.isDefined) {
            gotDataPromise.success(assert(data.get.rates.nonEmpty))
          }
        }, Seq(data))

        div()
      }
    }

    ReactDOM.render(
      ApolloProvider(
        client = new ApolloClient(
            ApolloClientOptions(
              uri = "https://graphql-currency-rates.glitch.me",
              cache = new InMemoryCache()
            )
          )
      )(
        UseQueryTestComponent()
      ),
      document.createElement("div")
    )

    gotDataPromise.future
  }

    test("Can make a Query with variables") {
    val gotDataPromise = Promise[Assertion]

    @react object UseQueryTestComponent {
      case class Props()

      val component = FunctionalComponent[Props] { _ =>
        val QueryResult(data, _, _) = useQuery[CurrencyRatesQuery.type](
          CurrencyRatesQuery.operation, 
          QueryHookOptions[CurrencyRatesQuery.type](variables = CurrencyRatesQuery.Variables("USD"))
        )

        useEffect(() => {
          if (data.isDefined) {
            gotDataPromise.success(assert(data.get.rates.nonEmpty))
          }
        }, Seq(data))

        div()
      }
    }

    ReactDOM.render(
      ApolloProvider(
        client = new ApolloClient(
            ApolloClientOptions(
              uri = "https://graphql-currency-rates.glitch.me",
              cache = new InMemoryCache()
            )
          )
      )(
        UseQueryTestComponent()
      ),
      document.createElement("div")
    )

    gotDataPromise.future
  }
}
