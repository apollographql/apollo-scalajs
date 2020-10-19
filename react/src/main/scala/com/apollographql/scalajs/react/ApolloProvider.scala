package com.apollographql.scalajs.react

import scala.scalajs.js
import slinky.core.ExternalComponent
import slinky.core.annotations.react
import scala.scalajs.js.annotation.JSImport
import com.apollographql.scalajs.ApolloClient

@react object ApolloProvider extends ExternalComponent {
  case class Props(client: ApolloClient)

  @js.native
  @JSImport("@apollo/client", "ApolloProvider")
  val apolloProviderComponent: js.Object = js.native

  override val component = apolloProviderComponent
}
