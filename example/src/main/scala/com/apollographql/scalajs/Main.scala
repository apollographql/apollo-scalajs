package com.apollographql.scalajs

import com.apollographql.scalajs.react.ApolloProvider
import slinky.web.ReactDOM
import slinky.web.html._
import slinky.hot
import org.scalajs.dom.{document, html}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.scalajs.LinkingInfo
import com.apollographql.scalajs.cache.InMemoryCache

object Main {
  @JSExportTopLevel("main")
  def main(): Unit = {
    if (LinkingInfo.developmentMode) {
      hot.initialize()
    }

    if (js.typeOf(js.Dynamic.global.window.reactContainer) == "undefined") {
      js.Dynamic.global.window.reactContainer = document.createElement("div")
      document.body.appendChild(js.Dynamic.global.window.reactContainer.asInstanceOf[html.Element])
    }

    val client = new ApolloClient(
      ApolloClientOptions(
        uri = "https://graphql-todo-tracker.glitch.me",
        cache = new InMemoryCache()
      )
    )

    ReactDOM.render(
      ApolloProvider(client)(
        TodosView()
      ),
      js.Dynamic.global.window.reactContainer.asInstanceOf[html.Element]
    )
  }
}
