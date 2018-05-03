# react-apollo-scalajs
_use Apollo Client from your Scala.js React apps!_

## View the [docs](https://github.com/apollographql/react-apollo-scalajs/tree/master/docs/source)

## Installation
Add the dependency to your build.sbt
```scala
resolvers += "Apollo Bintray" at "https://dl.bintray.com/apollographql/maven/"
libraryDependencies += "com.apollographql" %%% "apollo-scalajs-react" % "0.4.0"
```

You probably also want to add other Slinky modules such as the web module, so check out the instructions at https://slinky.shadaj.me

To set up the code generator, which uses `apollo-codegen` to generate static types for your GraphQL queries, first install `apollo-codegen`
```npm i -g apollo-codegen```

and then set up SBT to automatically run it

```scala
val namespace = "your package here"

(sourceGenerators in Compile) += Def.task {
  import scala.sys.process._

  val out = (sourceManaged in Compile).value

  out.mkdirs()

  Seq(
    "apollo-codegen", "generate", ((sourceDirectory in Compile).value / "graphql").getAbsolutePath + "/*.graphql",
    "--schema", (baseDirectory.value / "schema.json").getAbsolutePath,
    "--target", "scala",
    "--namespace", namespace,
    "--output", (out / "graphql.scala").getAbsolutePath
  ).!
  Seq(out / "graphql.scala")
}
```

## Usage
Once you have placed some GraphQL queries in `src/main/graphql`, you can use the generated types to create GraphQL-powered React components!

To integrate GraphQL data in your React tree, simply use the `Query` component to render subtrees based on a specified query.

```scala
Query(UsdRatesQuery)  { queryStatus =>
  if (queryStatus.loading) "Loading..."
  else if (queryStatus.error) s"Error! ${queryStatus.error.message}"
  else {
    div(queryStatus.data.get.rates.mkString(", "))
  }
}
```

For more on implementing advanced components, follow the instructions at https://slinky.shadaj.me

Next, to initialize Apollo Client in your application, first create an instance of the client

```scala
val client = ApolloBoostClient(
  uri = "https://w5xlvm3vzz.lp.gql.zone/graphql"
)
```

Finally, wrap your React component tree inside an `ApolloProvider` component, which all components inside to perform GraphQL queries with the specified client

```scala
ApolloProvider(client)(
  ...
)
```
