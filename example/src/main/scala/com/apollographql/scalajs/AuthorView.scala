package com.apollographql.scalajs

import me.shadaj.slinky.core.Component
import me.shadaj.slinky.core.facade.ComponentInstance
import me.shadaj.slinky.core.html._

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

object AuthorView extends Component {
  case class ExtraProps(id: Int)
  type Props = AuthorQuery.Props#WithExtra[ExtraProps]
  type State = Unit

  @ScalaJSDefined
  class Def(jsProps: js.Object) extends Definition(jsProps) {
    override def initialState: Unit = ()

    override def render(): ComponentInstance = {
      props.data.fold[ComponentInstance](
        h1("loading!")
      ) { d =>
        div(
          d.author.toString
        )
      }
    }
  }

  val WithData = graphqlWithVariables(
    AuthorQuery
  )((e: AuthorView.ExtraProps) => Some(AuthorQuery.Variables(e.id)))(this)
}
