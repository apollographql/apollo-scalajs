package com.apollographql.scalajs.react

import com.apollographql.scalajs.{AddTodoMutation, ApolloBoostClient, UnfetchFetch, gql}
import org.scalajs.dom.document
import org.scalatest.{Assertion, AsyncFunSuite}
import slinky.web.ReactDOM
import slinky.web.html.div

import scala.concurrent.{Future, Promise}
import scala.scalajs.js
import scala.util.{Failure, Success}

class MutationComponentTest extends AsyncFunSuite {
  js.Dynamic.global.fetch = UnfetchFetch

  implicit override def executionContext =
    scala.concurrent.ExecutionContext.Implicits.global

  test("Can mount a Mutation component and call the mutation") {
    val gotDataPromise = Promise[Assertion]
    case class Todo(typ: String)
    case class TodoResult(addTodo: Todo)

    var callMutation: () => Unit = null

    ReactDOM.render(
      ApolloProvider(
        client = ApolloBoostClient(uri = "https://graphql-todo-tracker.glitch.me")
      )(
        Mutation[TodoResult, Unit](gql(
          """mutation {
            |  addTodo(type: "lol") {
            |    typ: type
            |  }
            |}""".stripMargin
        )) { (mut, d) =>
          if (d.data.isDefined) {
            gotDataPromise.success(assert(d.data.get.addTodo.typ == "lol"))
          }

          callMutation = () => {
            mut()
          }

          div()
        }
      ),
      document.createElement("div")
    )

    callMutation()

    gotDataPromise.future
  }

  test("Can mount a Mutation component and get an error when the mutation fails") {
    val gotFailurePromise = Promise[Assertion]

    var ranMutation = false

    ReactDOM.render(
      ApolloProvider(
        client = ApolloBoostClient(uri = "https://graphql-todo-tracker.glitch.me")
      )(
        Mutation[Unit, Unit](gql(
          """mutation {
            |  randomMutationThatDoesntExist(type: "lol") {
            |    typ: type
            |  }
            |}""".stripMargin
        )) { (mut, d) =>
          if (!ranMutation) {
            ranMutation = true
            mut().andThen {
              case Success(_) =>
                gotFailurePromise.failure(new Exception("Succeeded when it shouldn't have"))
              case Failure(_) =>
                gotFailurePromise.success(assert(true))
            }
          }

          div()
        }
      ),
      document.createElement("div")
    )

    gotFailurePromise.future
  }

  test("Can mount a Mutation component and call the mutation with variables") {
    val gotDataPromise = Promise[Assertion]
    case class Todo(typ: String)
    case class TodoResult(addTodo: Todo)
    case class Variables(typ: String)

    var callMutation: () => Unit = null

    ReactDOM.render(
      ApolloProvider(
        client = ApolloBoostClient(uri = "https://graphql-todo-tracker.glitch.me")
      )(
        Mutation[TodoResult, Variables](gql(
          """mutation AddTodo($typ: String!) {
            |  addTodo(type: $typ) {
            |    typ: type
            |  }
            |}""".stripMargin
        )) { (mut, d) =>
          if (d.data.isDefined) {
            gotDataPromise.success(assert(d.data.get.addTodo.typ == "bar"))
          }

          callMutation = () => {
            mut(Variables(typ = "bar"))
          }

          div()
        }
      ),
      document.createElement("div")
    )

    callMutation()

    gotDataPromise.future
  }

  test("Can mount a Mutation component, call the mutation, and get the result from the future") {
    case class Todo(typ: String)
    case class TodoResult(addTodo: Todo)
    case class Variables(typ: String)

    var resultFuture: Future[Assertion] = null

    ReactDOM.render(
      ApolloProvider(
        client = ApolloBoostClient(uri = "https://graphql-todo-tracker.glitch.me")
      )(
        Mutation[TodoResult, Variables](gql(
          """mutation AddTodo($typ: String!) {
            |  addTodo(type: $typ) {
            |    typ: type
            |  }
            |}""".stripMargin
        )) { (mut, _) =>
          if (resultFuture == null) {
            resultFuture = mut(Variables(typ = "bar")).map { d =>
              assert(d.data.addTodo.typ == "bar")
            }
          }

          div()
        }
      ),
      document.createElement("div")
    )

    resultFuture
  }

  test("Can mount a Mutation component with a static mutation and call the mutation with variables") {
    val gotDataPromise = Promise[Assertion]

    var callMutation: () => Unit = null

    ReactDOM.render(
      ApolloProvider(
        client = ApolloBoostClient(uri = "https://graphql-todo-tracker.glitch.me")
      )(
        Mutation(AddTodoMutation) { (mut, res) =>
          if (res.data.isDefined) {
            gotDataPromise.success(assert(res.data.get.addTodo.get.typ == "bar"))
          }

          callMutation = () => {
            mut(AddTodoMutation.Variables(typ = "bar"))
          }

          div()
        }
      ),
      document.createElement("div")
    )

    callMutation()

    gotDataPromise.future
  }
}
