package com.apollographql.scalajs

trait GraphQLQuery {
  val operation: DocumentNode

  type Data
  type Variables
}

trait GraphQLMutation {
  val operation: DocumentNode

  type Data
  type Variables
}
