# react-apollo-scalajs
_use Apollo Client from your Scala.js React apps!_

## Installation
Add the dependency to your build.sbt
```scala
libraryDependencies += "com.apollographql" %%% "react-apollo-scalajs" % "0.1.0"
```

You probably also want to add other Slinky modules such as the web module, so check out the instructions at http://github.com/shadaj/slinky

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

When creating a React component that will receive data from Apollo, the only special step is specifying the `Props` type to be the `Data` type inside your query object and creating a `WithData` component through the `graphql` higher-order-component.

```scala
import com.apollographql.scalajs._

object MyComponent extends Component {
  type Props = MyQuery.Data
  ...
  
  val WithData = graphql(MyQuery)(this)
}
```

For implementing the rest of your component, follow the instructions in http://github.com/shadaj/slinky

Next, to initialize Apollo Client in your application, first create an instance of the client

```scala
val client = ApolloClient(ApolloClientOptions())
```

Next, wrap your React component tree inside an `ApolloProvider` component, which all components inside to perform GraphQL queries with the specified client

```scala
ApolloProvider(ApolloProvider.Props(client))(
  ...
)
```

