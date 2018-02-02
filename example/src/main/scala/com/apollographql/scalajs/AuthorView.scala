package com.apollographql.scalajs

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._


@react class AuthorView extends StatelessComponent {
  type Props = AuthorQuery.Props#WithExtra[AuthorView.ExtraProps]

  def render(): ReactElement = {
    props.data.map { d =>
      div(
        d.author.toString
      )
    }.getOrElse(h1("loading!"))
  }
}

object AuthorView {
  case class ExtraProps(id: Int)
  val WithData = graphql(
    AuthorQuery,
    (e: AuthorView.ExtraProps) => AuthorQuery.Variables(e.id)
  )(this)
}
