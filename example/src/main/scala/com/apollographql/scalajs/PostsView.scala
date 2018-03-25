package com.apollographql.scalajs

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class PostsView extends StatelessComponent {
  case class Props()

  def render(): ReactElement = {
    Query(AllPostsQuery) { res =>
      res.data.map { d =>
        div(
          d.posts.toList.flatten.flatten.map { post =>
            div(key := post.id.toString)(
              h1(post.title.getOrElse[String]("???")),
              h2(post.votes.getOrElse(0).toString),
              UpVote(post.id)
            )
          }
        )
      }.getOrElse(h1("loading!"))
    }
  }
}
