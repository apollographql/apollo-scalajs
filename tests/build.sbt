enablePlugins(ScalaJSBundlerPlugin)

resolvers in Global += Resolver.sonatypeRepo("releases")

libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.3" % Test
libraryDependencies += "me.shadaj" %%% "slinky-web" % "0.4.3" % Test

npmDependencies in Test += "react" -> "16.2.0"
npmDependencies in Test += "react-dom" -> "16.2.0"

npmDependencies in Compile += "apollo-boost" -> "0.1.3"
npmDependencies in Compile += "react-apollo" -> "2.1.0"
npmDependencies in Compile += "graphql-tag" -> "2.8.0"
npmDependencies in Compile += "graphql" -> "0.13.2"

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
    "apollo", "codegen:generate", s"--queries=${((sourceDirectory in Test).value / "graphql" / "queries").getAbsolutePath}/*.graphql",
    s"--schema=${(baseDirectory.value / "queries.json").getAbsolutePath}",
    "--namespace", namespace,
    graphQLScala.getAbsolutePath
  ).!

  Seq(graphQLScala)
}

(sourceGenerators in Test) += Def.task {
  import scala.sys.process._

  val out = (sourceManaged in Test).value

  out.mkdirs()

  val graphQLScala = out / "mutations.scala"

  Seq(
    "apollo", "codegen:generate", s"--queries=${((sourceDirectory in Test).value / "graphql" / "mutations").getAbsolutePath}/*.graphql",
    s"--schema=${(baseDirectory.value / "mutations.json").getAbsolutePath}",
    "--namespace", namespace,
    graphQLScala.getAbsolutePath
  ).!

  Seq(graphQLScala)
}

watchSources in Test ++= ((sourceDirectory in Test).value / "graphql" ** "*.graphql").get
