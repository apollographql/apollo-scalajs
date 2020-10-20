package com.apollographql.scalajs.link

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

trait HttpLinkOptions extends js.Object {
  val uri: String
  val fetch: js.UndefOr[js.Object] = js.undefined
}

object HttpLinkOptions {
  def apply(uri: String, fetch: js.UndefOr[js.Object] = js.undefined): HttpLinkOptions = {
    js.Dynamic.literal(
      uri = uri,
      fetch = fetch
    ).asInstanceOf[HttpLinkOptions]
  }
}

@JSImport("@apollo/client", "HttpLink")
@js.native
class HttpLink(options: HttpLinkOptions) extends ApolloLink
