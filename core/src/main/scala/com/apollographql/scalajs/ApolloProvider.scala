package com.apollographql.scalajs

import slinky.core.ExternalComponent
import slinky.core.annotations.react

@react object ApolloProvider extends ExternalComponent {
  case class Props(client: ApolloClientInstance)

  override val component = ReactApollo.ApolloProvider
}
