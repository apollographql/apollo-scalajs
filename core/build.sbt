enablePlugins(ScalaJSPlugin)

name := "react-apollo-scalajs"

libraryDependencies += "me.shadaj" %%% "slinky-core" % "0.2.0"

addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full)
