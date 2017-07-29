package com.apollographql.scalajs

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.facade.ComponentInstance
import me.shadaj.slinky.core.html._

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

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
          d.posts.toList.flatten.flatten.map { post =>
            div(key := post.id.toString)(
              h1(post.title.getOrElse[String]("???")),
              h2(post.votes.getOrElse(0).toString),
              UpVote.WithData(UpVote.ExtraProps(post.id, v => {
                println(s"Upvote result: $v")
              }))
            )
          }
        )
      }
    }
  }

  val WithData = graphql(AllPostsQuery)(this)
}
