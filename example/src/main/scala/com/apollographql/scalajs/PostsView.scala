package com.apollographql.scalajs

import me.shadaj.slinky.core.{Component, TagComponent}
import me.shadaj.slinky.core.facade.ReactElement
import me.shadaj.slinky.web.html._

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

object PostsView extends Component {
  type Props = AllPostsQuery.Props
  type State = Unit

  @ScalaJSDefined
  class Def(jsProps: js.Object) extends Definition(jsProps) {
    override def initialState: Unit = ()

    override def render(): ReactElement = {
      props.data.map { d =>
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
      }.getOrElse[TagComponent[Any]](h1("loading!"))
    }
  }

  val WithData = graphql(AllPostsQuery)(this)
}
