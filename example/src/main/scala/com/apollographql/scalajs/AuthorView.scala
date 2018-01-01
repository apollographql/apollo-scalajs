package com.apollographql.scalajs

import me.shadaj.slinky.core.{ComponentWrapper, TagComponent}
import me.shadaj.slinky.core.facade.ReactElement
import me.shadaj.slinky.web.html._

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

object AuthorView extends ComponentWrapper {
  case class ExtraProps(id: Int)
  type Props = AuthorQuery.Props#WithExtra[ExtraProps]
  type State = Unit

  @ScalaJSDefined
  class Def(jsProps: js.Object) extends Definition(jsProps) {
    override def initialState: Unit = ()

    override def render(): ReactElement = {
      props.data.map { d =>
        div(
          d.author.toString
        )
      }.getOrElse[TagComponent[Any]](h1("loading!"))
    }
  }

  val WithData = graphqlWithVariables(
    AuthorQuery
  )((e: AuthorView.ExtraProps) => Some(AuthorQuery.Variables(e.id)))(this)
}
