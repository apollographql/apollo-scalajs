package com.apollographql.scalajs

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
trait DocumentNode extends js.Object

@js.native
@JSImport("@apollo/client", "gql")
object gql extends js.Object {
  def apply(query: String): DocumentNode = js.native
}
