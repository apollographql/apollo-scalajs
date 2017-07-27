package me.shadaj.apollo

trait GraphQLQuery {
  val operation: Query

  type Data
  type Variables

  type Props = ApolloQueryProps[Data, Unit]
}

trait GraphQLMutation {
  val operation: Query

  type Data
  type Variables

  type Props = ApolloMutationProps[Variables, Data, Unit]
}
