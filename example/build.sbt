enablePlugins(ScalaJSBundlerPlugin)

name := "react-apollo-scalajs-example"

libraryDependencies += "me.shadaj" %%% "slinky-web" % "0.4.1"
libraryDependencies += "me.shadaj" %%% "slinky-hot" % "0.4.1"

npmDependencies in Compile += "react" -> "16.2.0"
npmDependencies in Compile += "react-dom" -> "16.2.0"
npmDependencies in Compile += "react-proxy" -> "1.1.8"

npmDependencies in Compile += "apollo-boost" -> "0.1.3"
npmDependencies in Compile += "react-apollo" -> "2.1.0"
npmDependencies in Compile += "graphql-tag" -> "2.8.0"
npmDependencies in Compile += "graphql" -> "0.13.2"

npmDevDependencies in Compile += "file-loader" -> "1.1.5"
npmDevDependencies in Compile += "style-loader" -> "0.19.0"
npmDevDependencies in Compile += "css-loader" -> "0.28.7"
npmDevDependencies in Compile += "html-webpack-plugin" -> "2.30.1"
npmDevDependencies in Compile += "copy-webpack-plugin" -> "4.2.0"

scalacOptions += "-P:scalajs:sjsDefinedByDefault"

addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M11" cross CrossVersion.full)

webpackConfigFile in fastOptJS := Some(baseDirectory.value / "webpack-fastopt.config.js")
webpackConfigFile in fullOptJS := Some(baseDirectory.value / "webpack-opt.config.js")

webpackDevServerExtraArgs := Seq("--inline", "--hot")

webpackBundlingMode in fastOptJS := BundlingMode.LibraryOnly()


val namespace = "com.apollographql.scalajs"

(sourceGenerators in Compile) += Def.task {
  import scala.sys.process._

  val out = (sourceManaged in Compile).value

  out.mkdirs()

  val graphQLScala = out / "graphql.scala"

  Seq(
    "apollo-codegen", "generate", ((sourceDirectory in Compile).value / "graphql").getAbsolutePath + "/*.graphql",
    "--schema", (baseDirectory.value / "schema.json").getAbsolutePath,
    "--target", "scala",
    "--namespace", namespace,
    "--output", graphQLScala.getAbsolutePath
  ).!

  // complete hack to get around apollo-codegen 0.18.3 not actually generating the package namespace
  sbt.IO.writeLines(graphQLScala, s"package $namespace" +: sbt.IO.readLines(graphQLScala))

  Seq(graphQLScala)
}

watchSources ++= ((sourceDirectory in Compile).value / "graphql" ** "*.graphql").get
