package com.apollographql.scalajs

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.facade.ReactElement
import me.shadaj.slinky.web.html._

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

import scala.concurrent.ExecutionContext.Implicits.global

object UpVote extends Component {
  case class ExtraProps(postId: Int, update: UpVoteMutation.Data => Unit)
  type Props = UpVoteMutation.Props#WithExtra[ExtraProps]
  type State = Boolean

  @ScalaJSDefined
  class Def(jsProps: js.Object) extends Definition(jsProps) {
    override def initialState = false

    override def render(): ReactElement = {
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

  val WithData = graphql(UpVoteMutation)(this)
}
