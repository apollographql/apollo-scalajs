package com.apollographql.scalajs.react

import com.apollographql.scalajs.ApolloClientInstance
import slinky.core.ExternalComponent
import slinky.core.annotations.react

@react object ApolloProvider extends ExternalComponent {
  case class Props(client: ApolloClientInstance)

  override val component = TopLevelReactApollo.ApolloProvider
}
