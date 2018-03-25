package com.apollographql.scalajs

import slinky.web.ReactDOM
import slinky.web.html._
import slinky.hot

import org.scalajs.dom.{document, html}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.scalajs.LinkingInfo

object Main {
  @JSExportTopLevel("entrypoint.main")
  def main(): Unit = {
    if (LinkingInfo.developmentMode) {
      hot.initialize()
    }

    if (js.typeOf(js.Dynamic.global.reactContainer) == "undefined") {
      js.Dynamic.global.reactContainer = document.createElement("div")
      document.body.appendChild(js.Dynamic.global.reactContainer.asInstanceOf[html.Element])
    }

    val client = ApolloBoostClient(
      uri = "https://1jzxrj179.lp.gql.zone/graphql"
    )

    ReactDOM.render(
      ApolloProvider(client)(
        div(
          PostsView(),
          AuthorView(1)
        )
      ),
      js.Dynamic.global.reactContainer.asInstanceOf[html.Element]
    )
  }
}
