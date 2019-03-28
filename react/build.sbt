enablePlugins(ScalaJSPlugin)

name := "apollo-scalajs-react"

libraryDependencies += "me.shadaj" %%% "slinky-core" % "0.6.0"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
