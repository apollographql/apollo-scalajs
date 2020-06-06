val scalaJSVersion =
  Option(System.getenv("SCALAJS_VERSION")).getOrElse("0.6.32")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % scalaJSVersion)

{
  if (scalaJSVersion.startsWith("0.6.")) addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler-sjs06" % "0.18.0")
  else Seq(addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.18.0"))
}

addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.6")

addSbtPlugin("com.dwijnand" % "sbt-dynver" % "4.0.0")
