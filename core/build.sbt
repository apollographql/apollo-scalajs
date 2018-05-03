enablePlugins(ScalaJSPlugin)

name := "apollo-scalajs-core"

libraryDependencies += "me.shadaj" %%% "slinky-readwrite" % "0.4.1"

addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full)
