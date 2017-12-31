enablePlugins(ScalaJSBundlerPlugin)

name := "react-apollo-scalajs-example"

libraryDependencies += "me.shadaj" %%% "slinky-web" % "0.2.0"

npmDependencies in Compile += "react" -> "15.6.2"

npmDependencies in Compile += "react-dom" -> "15.6.2"

npmDependencies in Compile += "react-apollo" -> "1.4.15"

scalaJSUseMainModuleInitializer := true

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

  Seq(
    "apollo-codegen", "generate", ((sourceDirectory in Compile).value / "graphql").getAbsolutePath + "/*.graphql",
    "--schema", (baseDirectory.value / "schema.json").getAbsolutePath,
    "--target", "scala",
    "--namespace", namespace,
    "--output", (out / "graphql.scala").getAbsolutePath
  ).!
  Seq(out / "graphql.scala")
}

watchSources ++= ((sourceDirectory in Compile).value / "graphql" ** "*.graphql").get
