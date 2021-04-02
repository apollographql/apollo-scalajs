---
title: Installation
order: 0
---

Add the dependency to your `build.sbt`

```scala
resolvers += "Apollo Bintray" at "https://dl.bintray.com/apollographql/maven/"
libraryDependencies += "com.apollographql" %%% "apollo-scalajs-core" % "0.8.0"
libraryDependencies += "com.apollographql" %%% "apollo-scalajs-react" % "0.8.0"
```

If you are using the React API, you may want to add other Slinky modules such as the web and hot-reloading module, so check out the instructions at https://slinky.shadaj.me/docs/installation/.

## NPM Dependencies
If you are using [Scala.js Bundler](https://scalacenter.github.io/scalajs-bundler/), you will need to add the Apollo Client dependencies to your `build.sbt`.

```scala
npmDependencies in Compile += "@apollo/client" -> "3.2.4"
npmDevDependencies in Compile += "apollo" -> "2.31.0"
```

## Code Generator
You can configure SBT to automatically run the code generator and add the resulting Scala sources to your build.

```scala
val namespace = "com.my.package.graphql"

(sourceGenerators in Compile) += Def.task {
  import scala.sys.process._

  val out = (sourceManaged in Compile).value
  val graphQLScala = out / "graphql.scala"

  out.mkdirs()

  Seq(
    "npx", "apollo", "client:codegen",
    "--config", "apollo.config.js",
    "--target", "scala",
    "--namespace", namespace, graphQLScala.getAbsolutePath
  ).!

  Seq(graphQLScala)
}

watchSources ++= ((sourceDirectory in Compile).value / "graphql" ** "*.graphql").get
```

With the accompanying [`apollo.config.js`](https://www.apollographql.com/docs/references/apollo-config.html):

```js
module.exports = {
  client: {
    service: {
      name: "test-schema",
      localSchemaFile: "schema.json"
    },
    includes: [ "src/main/graphql/*.graphql" ]
  }
};
```
