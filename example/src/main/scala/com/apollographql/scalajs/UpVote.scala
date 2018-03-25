package com.apollographql.scalajs

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class UpVote extends StatelessComponent {
  case class Props(postId: Int)

  def render(): ReactElement = {
    Mutation(UpVoteMutation) { (mut, res) =>
      button(
        onClick := (() => {
          mut(UpVoteMutation.Variables(props.postId))
        }),
        disabled := res.loading
      )(
        div(if (res.loading) "upvoting" else "upvote!")
      )
    }
  }
}
