package me.shadaj.apollo

import me.shadaj.slinky.core.facade.ReactDOM
import me.shadaj.slinky.core.html._

import scala.scalajs.js.JSApp
import org.scalajs.dom.document

object Main extends JSApp {
  override def main(): Unit = {
    val container = document.createElement("div")
    document.body.appendChild(container)

    val client = ApolloClient(ApolloClientOptions(
      networkInterface = Some(createNetworkInterface(NetworkInterfaceOptions(
        uri = Some("https://1jzxrj179.lp.gql.zone/graphql")
      )))
    ))

    ReactDOM.render(
      ApolloProvider(ApolloProvider.Props(client))(
        div(
          PostsView.WithData(()),
          AuthorView.WithData(AuthorView.ExtraProps(1))
        )
      ),
      container
    )
  }
}
