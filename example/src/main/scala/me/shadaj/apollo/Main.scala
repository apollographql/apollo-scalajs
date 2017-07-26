package me.shadaj.apollo

import me.shadaj.simple.react.core.{BuildingComponent, Component, Reader}
import me.shadaj.simple.react.core.facade.{ComponentInstance, ReactDOM}

import scala.scalajs.js.JSApp
import org.scalajs.dom.document
import me.shadaj.simple.react.core.html._

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends JSApp {
  object UpVote extends Component {
    case class ExtraProps(update: Unit => Unit)
    type Props = UpVoteMutation.Props#WithExtra[ExtraProps]
    type State = Unit

    @ScalaJSDefined
    class Def(jsProps: js.Object) extends Definition(jsProps) {
      override def initialState: Unit = ()

      override def render(): ComponentInstance = {
        button(onClick := (_ => {
          props.mutate(()).toFuture.foreach(v => {
            props.extraProps.update(())
          })
        }))
      }
    }
  }

  lazy val UpVoteWithData = graphql(UpVoteMutation)(UpVote)

  object PostsView extends Component {
    type Props = AllPostsQuery.Props
    type State = Unit

    @ScalaJSDefined
    class Def(jsProps: js.Object) extends Definition(jsProps) {
      override def initialState: Unit = ()

      override def render(): ComponentInstance = {
        props.data.fold[ComponentInstance](
          h1("loading!")
        ) { d =>
          div(
            d.posts.map { posts =>
              posts.flatten.map { post =>
                div(
                  h1(post.title.getOrElse[String]("???")),
                  h2(post.votes.getOrElse(0).toString)
                )
              }: HtmlComponentMod[divAttributeApplied]
            },
            UpVoteWithData(UpVote.ExtraProps((_) => {
//              props.refetch(())
            }))
          )
        }
      }
    }
  }

  lazy val PostsViewWithData = graphql(AllPostsQuery)(PostsView)

  override def main(): Unit = {
    val container = document.createElement("div")
    document.body.appendChild(container)

    val client = ApolloClient(ApolloClientOptions(
      networkInterface = Some(createNetworkInterface(NetworkInterfaceOptions(
        uri = Some("https://1jzxrj179.lp.gql.zone/graphql")
      )))
    ))

    ReactDOM.render(
      ApolloProvider(ApolloProvider.Props(client)).withChildren(
        div(
          PostsViewWithData()
        )
      ),
      container
    )
  }
}
