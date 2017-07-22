package me.shadaj.apollo

import me.shadaj.simple.react.core.{Component, Reader, WithRaw}
import me.shadaj.simple.react.core.fascade.{ComponentInstance, React, ReactDOM}

import scala.scalajs.js.{JSApp, JSON}
import org.scalajs.dom.{Event, document, html}
import me.shadaj.simple.react.core.html._

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

object Main extends JSApp {
  case class Hero(name: String, friends: js.Object)
  case class Data(hero: Hero)

  object Bar extends Component {
    type Props = ApolloData[Data]
    type State = Unit

    @ScalaJSDefined
    class Def extends Definition {
      override def initialState: Unit = ()

      override def render(): ComponentInstance = {
        props.data.map { d =>
          div(
            h1(d.toString)
          ): ComponentInstance
        }.getOrElse(h1("loading!"))
      }
    }
  }

  object Lol extends Component {
    type Props = ApolloData[Data]
    type State = Unit

    implicit val reader = implicitly[Reader[ApolloData[Data]]]

    @ScalaJSDefined
    class Def extends Definition {
      override def initialState: Unit = ()

      override def render(): ComponentInstance = {
        props.data.map { d =>
          div(
            h1(d.toString),
            BarWithData()
          ): ComponentInstance
        }.getOrElse(h1("loading!"))
      }
    }
  }

  val query = gql(
    """query {
      |  hero {
      |    name
      |    friends {
      |       name
      |    }
      |  }
      |}""".stripMargin)

  lazy val LolWithData = graphql(query)(Lol)
  lazy val BarWithData = graphql(query)(Bar)

  override def main(): Unit = {
    val container = document.createElement("div")
    document.body.appendChild(container)

    val ni = createNetworkInterface(js.Dynamic.literal(
      "uri" -> "https://mpjk0plp9.lp.gql.zone/graphql"
    ))

    val client = new ApolloClient(js.Dynamic.literal(
      "networkInterface" -> ni
    ))

    ReactDOM.render(
      ApolloProvider(ApolloProvider.Props(client))(
        div(
          LolWithData()
        )
      ),
      container
    )
  }
}
