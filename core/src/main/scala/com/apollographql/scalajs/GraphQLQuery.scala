package com.apollographql.scalajs

trait GraphQLQuery {
  val operation: ParsedQuery

  type Data
  type Variables
}

trait GraphQLMutation {
  val operation: ParsedQuery

  type Data
  type Variables
}
