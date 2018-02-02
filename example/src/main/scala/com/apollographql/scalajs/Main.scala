package com.apollographql.scalajs

import slinky.web.ReactDOM
import slinky.web.html._

import org.scalajs.dom.{document, html}

import scala.scalajs.js

object Main {

  def main(args: Array[String]): Unit = {
    if (js.typeOf(js.Dynamic.global.reactContainer) == "undefined") {
      js.Dynamic.global.reactContainer = document.createElement("div")
      document.body.appendChild(js.Dynamic.global.reactContainer.asInstanceOf[html.Element])
    }

    val client = ApolloClient(ApolloClientOptions(
      networkInterface = Some(createNetworkInterface(NetworkInterfaceOptions(
        uri = Some("https://1jzxrj179.lp.gql.zone/graphql")
      )))
    ))

    ReactDOM.render(
      ApolloProvider(client)(
        div(
          PostsView.WithData(()),
          AuthorView.WithData(AuthorView.ExtraProps(1))
        )
      ),
      js.Dynamic.global.reactContainer.asInstanceOf[html.Element]
    )
  }
}
