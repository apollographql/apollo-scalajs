enablePlugins(ScalaJSPlugin)

name := "react-apollo-scalajs"

libraryDependencies += "me.shadaj" %%% "slinky-core" % "0.3.2+4-f2220e6b"

addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full)
