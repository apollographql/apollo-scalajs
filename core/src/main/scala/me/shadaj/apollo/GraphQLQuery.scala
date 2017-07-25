package me.shadaj.apollo

trait GraphQLQuery {
  val operation: Query

  type Data

  type Props = ApolloQueryProps[Data, Unit]
}

trait GraphQLMutation {
  val operation: Query

  type Data
  type Variables = Unit

  type Props = ApolloMutationProps[Variables, Unit]
}
