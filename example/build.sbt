enablePlugins(ScalaJSBundlerPlugin)

name := "react-apollo-scalajs-example"

npmDependencies in Compile += "react" -> "15.6.1"

npmDependencies in Compile += "react-dom" -> "15.6.1"

npmDependencies in Compile += "react-apollo" -> "1.4.8"

scalaJSUseMainModuleInitializer := true
