package com.apollographql.scalajs

import slinky.core.Component
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

import scala.concurrent.ExecutionContext.Implicits.global

@react class UpVote extends Component {
  type Props = UpVoteMutation.Props#WithExtra[UpVote.ExtraProps]
  type State = Boolean

  def initialState = false

  def render(): ReactElement = {
    button(onClick := (() => {
      setState(true)
      props.mutate(UpVoteMutation.Variables(props.extraProps.postId)).foreach { r =>
        setState(false)
        props.extraProps.update(r)
      }
    }), disabled := state)(
      div(if (state) "upvoting" else "upvote!")
    )
  }
}

object UpVote {
  case class ExtraProps(postId: Int, update: UpVoteMutation.Data => Unit)
  val WithData = graphql(UpVoteMutation)(this)
}
