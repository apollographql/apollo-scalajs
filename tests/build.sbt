enablePlugins(ScalaJSBundlerPlugin)

libraryDependencies += "org.scalatest" %%% "scalatest" % "3.1.1" % Test
libraryDependencies += "me.shadaj" %%% "slinky-web" % "0.6.5" % Test

Test / npmDependencies += "react" -> "16.8.4"
Test / npmDependencies += "react-dom" -> "16.8.4"

Compile / npmDependencies += "apollo-boost" -> "0.1.16"
Compile / npmDependencies += "react-apollo" -> "2.2.2"
Compile / npmDependencies += "graphql-tag" -> "2.9.2"
Compile / npmDependencies += "graphql" -> "14.0.2"

Compile / npmDependencies += "unfetch" -> "2.1.1"

Test / requireJsDomEnv := true

scalacOptions ++= {
  if (scalaJSVersion.startsWith("0.6.")) Seq("-P:scalajs:sjsDefinedByDefault")
  else Nil
}

val namespace = "com.apollographql.scalajs"

(Test / sourceGenerators) += Def.task {
  import scala.sys.process._

  val out = (Test / sourceManaged).value

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

(Test / sourceGenerators) += Def.task {
  import scala.sys.process._

  val out = (Test / sourceManaged).value

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

Test / watchSources ++= ((Test / sourceDirectory).value / "graphql" ** "*.graphql").get

Test / scalacOptions ++= {
  if (scalaJSVersion.startsWith("0.6.")) Seq("-P:scalajs:sjsDefinedByDefault")
  else Nil
}

scalaJSLinkerConfig ~= { _.withESFeatures(_.withUseECMAScript2015(false)) }
