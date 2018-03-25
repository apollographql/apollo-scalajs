package com.apollographql.scalajs

import slinky.core.Component
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

import scala.concurrent.ExecutionContext.Implicits.global

@react class UpVote extends Component {
  case class Props(postId: Int, update: UpVoteMutation.Data => Unit)
  type State = Boolean

  def initialState = false

  def render(): ReactElement = {
    Mutation(UpVoteMutation) { (mut, _) =>
      button(
        onClick := (() => {
          setState(true)
          mut(UpVoteMutation.Variables(props.postId)).foreach { r =>
            setState(false)
            props.update(r.data)
          }
        }),
        disabled := state
      )(
        div(if (state) "upvoting" else "upvote!")
      )
    }
  }
}
