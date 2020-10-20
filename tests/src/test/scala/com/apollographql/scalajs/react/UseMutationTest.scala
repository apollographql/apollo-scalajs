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

class UseMutationTest extends AsyncFunSuite {
  js.Dynamic.global.window.fetch = UnfetchFetch

  implicit override def executionContext =
    scala.concurrent.ExecutionContext.Implicits.global

  test("Can make simple mutation") {
    val gotDataPromise = Promise[Assertion]

    @react object UseMutationTestComponent {
      case class Props()

      val component = FunctionalComponent[Props] { _ =>
        val (mut, result) = {
          val hook = useMutation[AddTodoMutation.type](
            AddTodoMutation.operation
          )

          (
            (variables: AddTodoMutation.Variables) => {
              hook._1.apply(MutationHookOptions[AddTodoMutation.type](variables = variables)).toFuture
            },
            hook._2
          )
        }

        mut(AddTodoMutation.Variables(typ = "test"))

        useEffect(() => {
          if (result.data.isDefined) {
            gotDataPromise.success(assert(result.data.get.addTodo.nonEmpty))
          }
        }, Seq(result))

        div()
      }
    }

    ReactDOM.render(
      ApolloProvider(
        client = new ApolloClient(
            ApolloClientOptions(
              uri = "https://graphql-todo-tracker.glitch.me",
              cache = new InMemoryCache()
            )
          )
      )(
        UseMutationTestComponent()
      ),
      document.createElement("div")
    )

    gotDataPromise.future
  }
}
