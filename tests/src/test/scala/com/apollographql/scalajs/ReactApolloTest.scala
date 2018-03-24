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

  test("Can mount a Query component and render data based on the query") {
    val gotDataPromise = Promise[Assertion]
    case class ResultShape(rates: Seq[js.Object])
    ReactDOM.render(
      ApolloProvider(
        client = ApolloBoostClient(uri = "https://w5xlvm3vzz.lp.gql.zone/graphql")
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
        client = ApolloBoostClient(uri = "https://w5xlvm3vzz.lp.gql.zone/graphql")
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
        client = ApolloBoostClient(uri = "https://w5xlvm3vzz.lp.gql.zone/graphql")
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
        client = ApolloBoostClient(uri = "https://w5xlvm3vzz.lp.gql.zone/graphql")
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
        client = ApolloBoostClient(uri = "https://w5xlvm3vzz.lp.gql.zone/graphql")
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

  test("Can mount a Mutation component and call the mutation") {
    val gotDataPromise = Promise[Assertion]
    case class Todo(typ: String)
    case class TodoResult(addTodo: Todo)

    var callMutation: () => Unit = null

    ReactDOM.render(
      ApolloProvider(
        client = ApolloBoostClient(uri = "https://8v9r9kpn7q.lp.gql.zone/graphql")
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

  test("Can mount a Mutation component and call the mutation with variables") {
    val gotDataPromise = Promise[Assertion]
    case class Todo(typ: String)
    case class TodoResult(addTodo: Todo)
    case class Variables(typ: String)

    var callMutation: () => Unit = null

    ReactDOM.render(
      ApolloProvider(
        client = ApolloBoostClient(uri = "https://8v9r9kpn7q.lp.gql.zone/graphql")
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
        client = ApolloBoostClient(uri = "https://8v9r9kpn7q.lp.gql.zone/graphql")
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
        client = ApolloBoostClient(uri = "https://8v9r9kpn7q.lp.gql.zone/graphql")
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
