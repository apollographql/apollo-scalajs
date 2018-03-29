package com.apollographql.scalajs.cache

import slinky.readwrite.ObjectOrWritten

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

case class InMemoryCacheOptions()

@JSImport("apollo-cache-inmemory", "InMemoryCache")
@js.native
class InMemoryCache(options: js.UndefOr[ObjectOrWritten[InMemoryCacheOptions]] = js.undefined) extends ApolloCache
