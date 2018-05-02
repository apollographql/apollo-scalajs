enablePlugins(ScalaJSPlugin)

name := "apollo-scalajs-react"

libraryDependencies += "me.shadaj" %%% "slinky-core" % "0.4.0"

addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full)
