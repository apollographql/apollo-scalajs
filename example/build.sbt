enablePlugins(ScalaJSBundlerPlugin)

name := "react-apollo-scalajs-example"

libraryDependencies += "me.shadaj" %%% "slinky-web" % "0.3.0"
libraryDependencies += "me.shadaj" %%% "slinky-hot" % "0.3.0"

npmDependencies in Compile += "react" -> "16.2.0"

npmDependencies in Compile += "react-dom" -> "16.2.0"

npmDependencies in Compile += "react-apollo" -> "1.4.15"

npmDependencies in Compile += "react-proxy" -> "1.1.8"

webpackConfigFile in fastOptJS := Some(baseDirectory.value / "webpack-fastopt.config.js")
webpackConfigFile in fullOptJS := Some(baseDirectory.value / "webpack-opt.config.js")

emitSourceMaps := false

webpackDevServerExtraArgs := Seq("--inline", "--hot")

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

scalacOptions += "-P:scalajs:sjsDefinedByDefault"

addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M11" cross CrossVersion.full)
