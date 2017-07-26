enablePlugins(ScalaJSBundlerPlugin)

name := "react-apollo-scalajs-example"

npmDependencies in Compile += "react" -> "15.6.1"

npmDependencies in Compile += "react-dom" -> "15.6.1"

npmDependencies in Compile += "react-apollo" -> "1.4.8"

scalaJSUseMainModuleInitializer := true

val namespace = "me.shadaj.apollo"

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