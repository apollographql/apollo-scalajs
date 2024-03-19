enablePlugins(ScalaJSBundlerPlugin)
useYarn := true

name := "react-apollo-scalajs-example"

libraryDependencies += "me.shadaj" %%% "slinky-web" % "0.6.6"
libraryDependencies += "me.shadaj" %%% "slinky-hot" % "0.6.6"

npmDependencies in Compile += "react" -> "16.14.0"
npmDependencies in Compile += "react-dom" -> "16.14.0"
npmDependencies in Compile += "react-proxy" -> "1.1.8"

npmDependencies in Compile += "@apollo/client" -> "3.2.4"

npmDevDependencies in Compile += "apollo" -> "2.31.0"
npmDevDependencies in Compile += "file-loader" -> "1.1.5"
npmDevDependencies in Compile += "style-loader" -> "0.19.0"
npmDevDependencies in Compile += "css-loader" -> "0.28.7"
npmDevDependencies in Compile += "html-webpack-plugin" -> "4.3.0"
npmDevDependencies in Compile += "copy-webpack-plugin" -> "5.1.1"

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
    "npx", "apollo", "client:codegen", s"--queries=${((sourceDirectory in Compile).value / "graphql").getAbsolutePath}/*.graphql",
    s"--localSchemaFile=${(baseDirectory.value / "schema.json").getAbsolutePath}",
    "--target=scala",
    s"--namespace=$namespace",
    graphQLScala.getAbsolutePath
  ).!

  Seq(graphQLScala)
}

watchSources ++= ((sourceDirectory in Compile).value / "graphql" ** "*.graphql").get
