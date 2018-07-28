---
title: Installation
order: 0
---

Add the dependency to your `build.sbt`
```scala
resolvers += "Apollo Bintray" at "https://dl.bintray.com/apollographql/maven/"
libraryDependencies += "com.apollographql" %%% "apollo-scalajs-core" % "0.4.0"
libraryDependencies += "com.apollographql" %%% "apollo-scalajs-react" % "0.4.0"
```

If you are using the React API, you may want to add other Slinky modules such as the web and hot-reloading module, so check out the instructions at https://slinky.shadaj.me/docs/installation/.

## NPM Dependencies
If you are using [Scala.js Bundler](https://scalacenter.github.io/scalajs-bundler/), you will need to add the Apollo Client dependencies to your `build.sbt`.
```scala
npmDependencies in Compile += "apollo-boost" -> "0.1.3"
npmDependencies in Compile += "react-apollo" -> "2.1.0"
npmDependencies in Compile += "graphql-tag" -> "2.8.0"
npmDependencies in Compile += "graphql" -> "0.13.2"
```

## Apollo CLI
To set up the code generator, which generates static types based on your GraphQL queries, first install the Apollo CLI.

```bash
npm install -g apollo
```

Then, you can configure SBT to automatically run it and add the resulting Scala sources to your build.

```scala
val namespace = "com.my.package.graphql"

(sourceGenerators in Compile) += Def.task {
  import scala.sys.process._

  val out = (sourceManaged in Compile).value

  out.mkdirs()

  Seq(
    "apollo", "codegen:generate", s"--queries=${((sourceDirectory in Compile).value / "graphql").getAbsolutePath}/*.graphql",
    s"--schema=${(baseDirectory.value / "schema.json").getAbsolutePath}",
    "--namespace", namespace,
    (out / "graphql.scala").getAbsolutePath
  ).!

  Seq(out / "graphql.scala")
}

watchSources ++= ((sourceDirectory in Compile).value / "graphql" ** "*.graphql").get
```
