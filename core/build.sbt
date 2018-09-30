enablePlugins(ScalaJSPlugin)

name := "apollo-scalajs-core"

libraryDependencies += "me.shadaj" %%% "slinky-readwrite" % "0.5.0"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
