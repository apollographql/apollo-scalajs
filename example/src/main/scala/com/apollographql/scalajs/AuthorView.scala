package com.apollographql.scalajs

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class AuthorView extends StatelessComponent {
  case class Props(id: Int)

  def render(): ReactElement = {
    Query(AuthorQuery, AuthorQuery.Variables(props.id)) {
      _.data.map { d =>
        div(
          d.author.toString
        )
      }.getOrElse(h1("loading!"))
    }
  }
}
