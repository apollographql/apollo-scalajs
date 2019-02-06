enablePlugins(ScalaJSPlugin)

name := "apollo-scalajs-core"

libraryDependencies += "me.shadaj" %%% "slinky-readwrite" % "0.5.2"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
