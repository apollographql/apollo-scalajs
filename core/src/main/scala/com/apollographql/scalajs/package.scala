package com.apollographql

import slinky.core._
import slinky.readwrite.{ObjectOrWritten, Reader, Writer}

import scala.scalajs.js
import scala.scalajs.js.ConstructorTag
import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

package object scalajs {
  type ApolloClient = ReactApolloFascade.ApolloClient
  object ApolloClient {
    // WHY INTELLIJ WHY
    def apply[O](v: O = js.undefined)(implicit ev: O => js.UndefOr[ObjectOrWritten[ApolloClientOptions]]): ApolloClient = new ApolloClient(ev(v))
  }

  // WHY INTELLIJ WHY
  def createNetworkInterface[O](options: O = js.undefined)
                               (implicit ev: O => js.UndefOr[ObjectOrWritten[NetworkInterfaceOptions]]): NetworkInterface = {
    ReactApolloFascade.createNetworkInterface(ev(options))
  }

  def gql(query: String): Query = ReactApolloFascade.gql(query)

  def graphql[D, V, E](query: Query, variables: E => Option[V])
                      (comp: BaseComponentWrapper { type Props = ApolloQueryProps[D, E] })
                      (implicit constructorTag: ConstructorTag[comp.Def],
                       writer: Writer[D], reader: Reader[D],
                       variablesWriter: Writer[V], variablesReader: Reader[V],
                       extraReader: Reader[E], extraWriter: Writer[E]): DataComponent[E] = {
    val dataWriter = implicitly[Writer[ApolloQueryProps[D, E]]]

    new DataComponent[E](ReactApolloFascade.graphql(query, js.Dynamic.literal(
      "props" -> ((obj: js.Object) => {
        val dyn = obj.asInstanceOf[js.Dynamic]
        val networkStatus = dyn.data.networkStatus.asInstanceOf[Int]
        val refetchData = implicitly[Reader[() => Future[DataResult]]].read(dyn.data.refetch.asInstanceOf[js.Object])
        dataWriter.write(ApolloQueryProps[D, E](
          if (networkStatus == 1) None else Some(reader.read(dyn.data.asInstanceOf[js.Object])),
          dyn.data.loading.asInstanceOf[Boolean],
          dyn.data.error.asInstanceOf[js.Object],
          networkStatus,
          () => refetchData().map(d => reader.read(d.data)),
          extraReader.read(dyn.ownProps.asInstanceOf[js.Object])
        ))
      }),
      "options" -> ((extra: js.Object) => {
        val parsedExtra = extraReader.read(extra)
        js.Dynamic.literal(
          "variables" -> implicitly[Writer[Option[V]]].write(variables(parsedExtra))
        )
      })
    ))(comp.componentConstructor))
  }

  def graphqlMutation[D, V, E](query: Query)
                              (comp: BaseComponentWrapper { type Props = ApolloMutationProps[V, D, E] })
                              (implicit constructorTag: ConstructorTag[comp.Def],
                               writer: Writer[D], reader: Reader[D],
                               extraReader: Reader[E], extraWriter: Writer[E],
                               variablesWriter: Writer[V], variablesReader: Reader[V]): DataComponent[E] = {
    val mutationWriter = implicitly[Writer[ApolloMutationProps[V, D, E]]]
    val mutateReader = implicitly[Reader[ApolloMutationOptions[V] => Future[D]]]
    new DataComponent[E](ReactApolloFascade.graphql(query, js.Dynamic.literal(
      "props" -> ((obj: js.Object) => {
        val dyn = obj.asInstanceOf[js.Dynamic]
        val mutationObj = ApolloMutationProps[V, D, E](
          mutate = mutateReader.read(dyn.mutate.asInstanceOf[js.Object]),
          extraReader.read(dyn.ownProps.asInstanceOf[js.Object])
        )
        val retBeforeAssign = mutationWriter.write(mutationObj)
        retBeforeAssign.asInstanceOf[js.Dynamic].__ = mutationObj.asInstanceOf[js.Object]
        retBeforeAssign
      })
    ))(comp.componentConstructor))
  }

  def graphql[E](query: GraphQLQuery)
                (comp: BaseComponentWrapper { type Props = ApolloQueryProps[query.Data, E] })
                (implicit constructorTag: ConstructorTag[comp.Def],
                 writer: Writer[query.Data], reader: Reader[query.Data],
                 variablesWriter: Writer[query.Variables], variablesReader: Reader[query.Variables],
                 extraReader: Reader[E], extraWriter: Writer[E]): DataComponent[E] = {
    graphql[query.Data, query.Variables, E](query.operation, _ => None)(comp)
  }

  def graphqlWithVariables[E](query: GraphQLQuery)
                             (variables: E => Option[query.Variables])
                             (comp: BaseComponentWrapper { type Props = ApolloQueryProps[query.Data, E] })
                             (implicit constructorTag: ConstructorTag[comp.Def],
                              writer: Writer[query.Data], reader: Reader[query.Data],
                              variablesWriter: Writer[query.Variables], variablesReader: Reader[query.Variables],
                              extraReader: Reader[E], extraWriter: Writer[E]): DataComponent[E] = {
    graphql[query.Data, query.Variables, E](query.operation, variables)(comp)
  }

  def graphql[E](query: GraphQLMutation)
                (comp: BaseComponentWrapper { type Props = ApolloMutationProps[query.Variables, query.Data, E]})
                (implicit constructorTag: ConstructorTag[comp.Def],
                 writer: Writer[query.Data], reader: Reader[query.Data],
                 variablesWriter: Writer[query.Variables], variablesReader: Reader[query.Variables]): DataComponent[E] = {
    graphqlMutation[query.Data, query.Variables, E](query.operation)(comp)
  }
}
