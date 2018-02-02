package com.apollographql.scalajs

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class PostsView extends StatelessComponent {
  type Props = AllPostsQuery.Props

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
    }.getOrElse(h1("loading!"))
  }
}

object PostsView {
  val WithData = graphql(AllPostsQuery)(this)
}
