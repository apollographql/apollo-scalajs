package com.apollographql.scalajs

import me.shadaj.slinky.core.{Component, TagComponent}
import me.shadaj.slinky.core.annotations.react
import me.shadaj.slinky.core.facade.ReactElement
import me.shadaj.slinky.web.html._


@react class AuthorView extends Component {
  type Props = AuthorQuery.Props#WithExtra[AuthorView.ExtraProps]
  type State = Unit

  def initialState: Unit = ()

  def render(): ReactElement = {
    props.data.map { d =>
      div(
        d.author.toString
      )
    }.getOrElse[TagComponent[Any]](h1("loading!"))
  }
}

object AuthorView {
  case class ExtraProps(id: Int)
  val WithData = graphqlWithVariables(
    AuthorQuery
  )((e: AuthorView.ExtraProps) => Some(AuthorQuery.Variables(e.id)))(this)
}
