enablePlugins(ScalaJSBundlerPlugin)

resolvers in Global += Resolver.sonatypeRepo("releases")

libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.3" % Test
libraryDependencies += "me.shadaj" %%% "slinky-web" % "0.6.0" % Test

npmDependencies in Test += "react" -> "16.8.4"
npmDependencies in Test += "react-dom" -> "16.8.4"

npmDependencies in Compile += "apollo-boost" -> "0.1.16"
npmDependencies in Compile += "react-apollo" -> "2.2.2"
npmDependencies in Compile += "graphql-tag" -> "2.9.2"
npmDependencies in Compile += "graphql" -> "14.0.2"

npmDependencies in Compile += "unfetch" -> "2.1.1"

jsDependencies += RuntimeDOM % Test

scalacOptions += "-P:scalajs:sjsDefinedByDefault"

val namespace = "com.apollographql.scalajs"

(sourceGenerators in Test) += Def.task {
  import scala.sys.process._

  val out = (sourceManaged in Test).value

  out.mkdirs()

  val graphQLScala = out / "queries.scala"

  Seq(
    "apollo", "client:codegen",
    "--config", "tests/src/test/graphql/queries/apollo.config.js",
    "--target", "scala",
    "--namespace", namespace, graphQLScala.getAbsolutePath
  ).!

  Seq(graphQLScala)
}

(sourceGenerators in Test) += Def.task {
  import scala.sys.process._

  val out = (sourceManaged in Test).value

  out.mkdirs()

  val graphQLScala = out / "mutations.scala"

  Seq(
    "apollo", "client:codegen",
    "--config", "tests/src/test/graphql/mutations/apollo.config.js",
    "--target", "scala",
    "--namespace", namespace, graphQLScala.getAbsolutePath
  ).!

  Seq(graphQLScala)
}

watchSources in Test ++= ((sourceDirectory in Test).value / "graphql" ** "*.graphql").get
