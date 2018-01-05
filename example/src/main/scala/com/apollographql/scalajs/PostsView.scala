package com.apollographql.scalajs

import me.shadaj.slinky.core.{Component, TagComponent}
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.core.facade.ReactElement
import me.shadaj.slinky.web.html._

@react class PostsView extends Component {
  type Props = AllPostsQuery.Props
  type State = Unit

  def initialState: Unit = ()

  def render(): ReactElement = {
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

object PostsView {
  val WithData = graphql(AllPostsQuery)(this)
}
