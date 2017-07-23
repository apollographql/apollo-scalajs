package me.shadaj.apollo

import me.shadaj.simple.react.core.Component
import me.shadaj.simple.react.core.fascade.{ComponentInstance, ReactDOM}

import scala.scalajs.js.JSApp
import org.scalajs.dom.document
import me.shadaj.simple.react.core.html._

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

object Main extends JSApp {
  case class HeroFriend(name: String, appearsIn: List[String])
  case class Hero(name: String, friends: Vector[HeroFriend])
  case class Data(hero: Hero)

  object Lol extends Component {
    type Props = ApolloData[Data]
    type State = Unit

    @ScalaJSDefined
    class Def(jsProps: js.Object) extends Definition(jsProps) {
      override def initialState: Unit = ()

      override def render(): ComponentInstance = {
        props.data.map { d =>
          div(
            h3(d.hero.toString),
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
      |       appearsIn
      |    }
      |  }
      |}""".stripMargin)

  lazy val LolWithData = graphql(query)(Lol)

  override def main(): Unit = {
    val container = document.createElement("div")
    document.body.appendChild(container)

    val client = ApolloClient(ApolloClientOptions(
      networkInterface = Some(createNetworkInterface(NetworkInterfaceOptions(
        uri = Some("https://mpjk0plp9.lp.gql.zone/graphql")
      )))
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
