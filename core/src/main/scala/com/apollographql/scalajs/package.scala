package com.apollographql

import slinky.core._
import slinky.readwrite.{ObjectOrWritten, Reader, Writer}

import scala.scalajs.js
import scala.scalajs.js.ConstructorTag
import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

package object scalajs {
  type ApolloClient = ReactApolloFacade.ApolloClient
  object ApolloClient {
    def apply(v: js.UndefOr[ObjectOrWritten[ApolloClientOptions]] = js.undefined): ApolloClient = new ApolloClient(v)
  }

  def createNetworkInterface(options: js.UndefOr[ObjectOrWritten[NetworkInterfaceOptions]] = js.undefined): NetworkInterface = {
    ReactApolloFacade.createNetworkInterface(options)
  }

  def gql(query: String): Query = ReactApolloFacade.gql(query)

  private def graphqlQuery[E, Q <: GraphQLQuery](query: Query, options: js.UndefOr[js.|[ApolloQueryOptions[Q], E => ApolloQueryOptions[Q]]])
                                   (comp: BaseComponentWrapper { type Props = ApolloQueryProps[Q#Data, E] })
                                   (implicit constructorTag: ConstructorTag[comp.Def],
                                    writer: Writer[Q#Data], reader: Reader[Q#Data],
                                    variablesWriter: Writer[Q#Variables], variablesReader: Reader[Q#Variables],
                                    extraReader: Reader[E], extraWriter: Writer[E]): DataComponent[E] = {
    val dataWriter = implicitly[Writer[ApolloQueryProps[Q#Data, E]]]
    val refetchReader = implicitly[Reader[() => Future[DataResult]]]
    val queryOptionsWriter = implicitly[Writer[js.UndefOr[js.|[ApolloQueryOptions[Q], E => ApolloQueryOptions[Q]]]]]

    new DataComponent[E](ReactApolloFacade.graphql(query, js.Dynamic.literal(
      "props" -> ((obj: js.Object) => {
        val dyn = obj.asInstanceOf[js.Dynamic]
        val networkStatus = dyn.data.networkStatus.asInstanceOf[Int]
        val refetchData = refetchReader.read(dyn.data.refetch.asInstanceOf[js.Object])
        val queryObj = ApolloQueryProps[Q#Data, E](
          if (networkStatus == 1) None else Some(reader.read(dyn.data.asInstanceOf[js.Object])),
          dyn.data.loading.asInstanceOf[Boolean],
          dyn.data.error.asInstanceOf[js.Object],
          networkStatus,
          () => refetchData().map(d => reader.read(d.data)),
          extraReader.read(dyn.ownProps.asInstanceOf[js.Object])
        )

        val retBeforeAssign = dataWriter.write(queryObj)
        retBeforeAssign.asInstanceOf[js.Dynamic].__ = queryObj.asInstanceOf[js.Object]
        retBeforeAssign
      }),
      "options" -> queryOptionsWriter.write(options)
    ))(comp.componentConstructor))
  }

  private def graphqlMutation[E, Q <: GraphQLMutation](query: Query)
                              (comp: BaseComponentWrapper { type Props = ApolloMutationProps[Q#Variables, Q#Data, E] })
                              (implicit constructorTag: ConstructorTag[comp.Def],
                               writer: Writer[Q#Data], reader: Reader[Q#Data],
                               extraReader: Reader[E], extraWriter: Writer[E],
                               variablesWriter: Writer[Q#Variables], variablesReader: Reader[Q#Variables]): DataComponent[E] = {
    val mutationWriter = implicitly[Writer[ApolloMutationProps[Q#Variables, Q#Data, E]]]
    val mutateReader = implicitly[Reader[ApolloMutationOptions[Q#Variables] => Future[Q#Data]]]
    new DataComponent[E](ReactApolloFacade.graphql(query, js.Dynamic.literal(
      "props" -> ((obj: js.Object) => {
        val dyn = obj.asInstanceOf[js.Dynamic]
        val mutationObj = ApolloMutationProps[Q#Variables, Q#Data, E](
          mutate = mutateReader.read(dyn.mutate.asInstanceOf[js.Object]),
          extraReader.read(dyn.ownProps.asInstanceOf[js.Object])
        )
        val retBeforeAssign = mutationWriter.write(mutationObj)
        retBeforeAssign.asInstanceOf[js.Dynamic].__ = mutationObj.asInstanceOf[js.Object]
        retBeforeAssign
      })
    ))(comp.componentConstructor))
  }

  def graphql[E, Q <: GraphQLQuery](query: Q, options: ApolloQueryOptions[Q])
                (comp: BaseComponentWrapper { type Props = ApolloQueryProps[Q#Data, E] })
                (implicit constructorTag: ConstructorTag[comp.Def],
                 writer: Writer[Q#Data], reader: Reader[Q#Data],
                 variablesWriter: Writer[Q#Variables], variablesReader: Reader[Q#Variables],
                 extraReader: Reader[E], extraWriter: Writer[E]): DataComponent[E] = {
    graphqlQuery[E, Q](
      query.operation,
      options
    )(comp)
  }

  def graphql[E, Q <: GraphQLQuery](query: Q, options: E => ApolloQueryOptions[Q])
                                   (comp: BaseComponentWrapper { type Props = ApolloQueryProps[Q#Data, E] })
                                   (implicit constructorTag: ConstructorTag[comp.Def],
                                    writer: Writer[Q#Data], reader: Reader[Q#Data],
                                    variablesWriter: Writer[Q#Variables], variablesReader: Reader[Q#Variables],
                                    extraReader: Reader[E], extraWriter: Writer[E]): DataComponent[E] = {
    graphqlQuery[E, Q](
      query.operation,
      options
    )(comp)
  }

  def graphql[E, Q <: GraphQLQuery](query: Q)
                                   (comp: BaseComponentWrapper { type Props = ApolloQueryProps[Q#Data, E] })
                                   (implicit constructorTag: ConstructorTag[comp.Def],
                                    writer: Writer[Q#Data], reader: Reader[Q#Data],
                                    variablesWriter: Writer[Q#Variables], variablesReader: Reader[Q#Variables],
                                    extraReader: Reader[E], extraWriter: Writer[E]): DataComponent[E] = {
    graphqlQuery[E, Q](
      query.operation,
      js.undefined
    )(comp)
  }

  def graphql[E, Q <: GraphQLMutation](query: Q, options: ApolloMutationOptions[Q#Variables])
                (comp: BaseComponentWrapper { type Props = ApolloMutationProps[Q#Variables, Q#Data, E]})
                (implicit constructorTag: ConstructorTag[comp.Def],
                 writer: Writer[Q#Data], reader: Reader[Q#Data],
                 variablesWriter: Writer[Q#Variables], variablesReader: Reader[Q#Variables]): DataComponent[E] = {
    graphqlMutation[E, Q](query.operation)(comp)
  }

  def graphql[E, Q <: GraphQLMutation](query: Q)
                                      (comp: BaseComponentWrapper { type Props = ApolloMutationProps[Q#Variables, Q#Data, E]})
                                      (implicit constructorTag: ConstructorTag[comp.Def],
                                       writer: Writer[Q#Data], reader: Reader[Q#Data],
                                       variablesWriter: Writer[Q#Variables], variablesReader: Reader[Q#Variables]): DataComponent[E] = {
    graphqlMutation[E, Q](query.operation)(comp)
  }
}
