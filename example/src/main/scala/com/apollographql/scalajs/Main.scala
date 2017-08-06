package com.apollographql.scalajs

import me.shadaj.slinky.web.ReactDOM
import me.shadaj.slinky.web.html._

import scala.scalajs.js.JSApp
import org.scalajs.dom.{document, html}

import scala.scalajs.js

object Main extends JSApp {
  override def main(): Unit = {
    if (js.Dynamic.global.reactContainer == js.undefined) {
      js.Dynamic.global.reactContainer = document.createElement("div")
      document.body.appendChild(js.Dynamic.global.reactContainer.asInstanceOf[html.Element])
    }

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
      js.Dynamic.global.reactContainer.asInstanceOf[html.Element]
    )
  }
}
