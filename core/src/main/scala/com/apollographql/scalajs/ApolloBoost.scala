package com.apollographql.scalajs

import slinky.readwrite.ObjectOrWritten

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

case class ApolloBoostClientOptions(uri: String,
                                    fetchOptions: js.UndefOr[js.Object] = js.undefined)

@JSImport("apollo-boost", JSImport.Default)
@js.native
class ApolloBoostClient(options: ObjectOrWritten[ApolloBoostClientOptions]) extends ApolloClientInstance
object ApolloBoostClient {
  def apply(uri: String) = new ApolloBoostClient(ApolloBoostClientOptions(uri = uri))
}
