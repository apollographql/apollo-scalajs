# Apollo Scala.js
_use Apollo Client and React Apollo from your Scala.js apps!_

## View the [docs](https://www.apollographql.com/docs/scalajs)

## Installation
Add the dependency to your build.sbt
```scala
resolvers += "Apollo Bintray" at "https://dl.bintray.com/apollographql/maven/"
libraryDependencies += "com.apollographql" %%% "apollo-scalajs-core" % "0.8.0" // if you are writing a vanilla Scala.js app
libraryDependencies += "com.apollographql" %%% "apollo-scalajs-react" % "0.8.0" // if you are writing a React Scala.js app
```

You probably also want to add other Slinky modules such as the web module, so check out the instructions at https://slinky.dev

To set up the code generator, which uses the Apollo CLI to generate static types for your GraphQL queries, first install `apollo`
```npm i -g apollo```

and then set up SBT to automatically run it

```scala
val namespace = "your package here"

(sourceGenerators in Compile) += Def.task {
  import scala.sys.process._

  val out = (sourceManaged in Compile).value

  out.mkdirs()

  Seq(
    "apollo", "client:codegen", s"--queries=${((sourceDirectory in Compile).value / "graphql").getAbsolutePath}/*.graphql",
    s"--localSchemaFile=${(baseDirectory.value / "schema.json").getAbsolutePath}",
    "--target=scala",
    s"--namespace=$namespace",
    graphQLScala.getAbsolutePath
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

Next, to initialize Apollo Client in your application, first create an instance of the client (here using Apollo Boost)

```scala
val client = ApolloBoostClient(
  uri = "https://graphql-currency-rates.glitch.me"
)
```

Finally, wrap your React component tree inside an `ApolloProvider` component, which all components inside to perform GraphQL queries with the specified client

```scala
ApolloProvider(client)(
  ...
)
```
