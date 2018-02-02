package com.apollographql.scalajs

trait GraphQLQuery {
  val operation: Query

  type Data
  type Variables

  type Props = ApolloQueryProps[Data, Any]
}

trait GraphQLMutation {
  val operation: Query

  type Data
  type Variables

  type Props = ApolloMutationProps[Variables, Data, Any]
}
