---
title: Getting Started
order: 1
---

Now that you have your build set up, let's start writing our first GraphQL application! 

## Create a client
First, we need to create an instance of Apollo Client, which will handle sending our GraphQL queries and caching the results (among many other features).

Inside your main function, we can use Apollo Boost to set up a client with a default HTTP link and in-memory cache.
```scala
import com.apollographql.scalajs._

def main(): Unit = { // called when the app launches
  val client = ApolloBoostClient(
    uri = "https://graphql-currency-rates.glitch.me"
  )
}
```

That’s it! Now your client is ready to start fetching data. Before we hook up Apollo Scala.js to React, let’s try sending a query with plain Scala first with the `client.query` function.

```scala
client.query[js.Object](gql( // gql is a member of the com.apollographql.scalajs package that parses your query
  """{
    |  rates(currency: "USD") {
    |    currency
    |  }
    |}""".stripMargin
))).foreach { result =>
  println(result.data)
}
```

If you open up the console, you should see the result of your GraphQL query. Now, let's learn how to connect Apollo Scala.js to React so we can start building query components with React Apollo.

## Connecting your client to React
To connect Apollo Client to React, you will need to use the `ApolloProvider` component, which can be found in the `com.apollographql.scalajs.react` package. For details on what the `ApolloProvider` component does, see the [React Apollo Docs](https://www.apollographql.com/docs/react/essentials/get-started/#creating-provider). We suggest placing your `ApolloProvider` somewhere high in your app, above any places where you need to access GraphQL data.

```scala
import com.apollographql.scalajs.react.ApolloProvider

ReactDOM.render(
  ApolloProvider(client)(
    ...
  ),
  rootElement
)
```

## Request data
Now that you have an `ApolloProvider` mounted, it's time to start performing queries with the `Query` component!

First, specify the type of your query result, then pass in your `gql`-parsed GraphQL query and a function that determines what to render as a curried parameter. To keep things simple, let's leave our component untyped by specifying `js.Object` as the return type.

The curried parameter is a function that takes the current query resolution result and returns a React tree. Because we don't always have the data requested, the `data` property on the result object is an `Option`.

```scala
import com.apollographql.scalajs.react.Query

Query[js.Object](gql(
  """{
    |  rates(currency: "USD") {
    |    currency
    |  }
    |}""".stripMargin
))) { result =>
  if (result.loading) {
    h1("Loading!")
  } else {
    div(result.data.get.toString)
  }
}
```

If you include this component in your React tree, you should see your GraphQL query result rendered to your screen.

## Next steps
Now that you’ve learned how to fetch data with Apollo Scala.js, you’re ready to dive deeper into creating more complex queries and mutations. After this section, we recommend moving onto:
+ [Queries](/essentials/queries/): Learn how to fetch queries with arguments and handle the results with static types
+ [Mutations](/essentials/mutations/): Learn how to update data with mutations and type your parameters and results