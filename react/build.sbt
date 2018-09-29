enablePlugins(ScalaJSPlugin)

name := "apollo-scalajs-react"

libraryDependencies += "me.shadaj" %%% "slinky-core" % "0.5.0"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
