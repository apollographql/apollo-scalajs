package me.shadaj.apollo

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.facade.ComponentInstance
import me.shadaj.slinky.core.html._

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

    override def render(): ComponentInstance = {
      button(onClick := (_ => {
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