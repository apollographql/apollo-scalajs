package com.apollographql.scalajs.react

import slinky.core.facade.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("react-apollo", JSImport.Namespace)
object ReactApollo extends js.Object {
  val ApolloProvider: js.Object = js.native
  val Query: js.Object = js.native
  val Mutation: js.Object = js.native
}


@js.native
@JSImport("react-apollo/renderToStringWithData.js", JSImport.Namespace)
object ReactApolloServer extends js.Object {
  def renderToStringWithData(component: ReactElement): js.Promise[String] = js.native
}
