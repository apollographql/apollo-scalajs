package com.apollographql.scalajs.link

import slinky.readwrite.ObjectOrWritten

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

case class HttpLinkOptions(uri: String)

@JSImport("@apollo/client", "HttpLink")
@js.native
class HttpLink(options: ObjectOrWritten[HttpLinkOptions]) extends ApolloLink
