package me.shadaj

import me.shadaj.slinky.core.{Component, ObjectOrWritten, Reader, Writer}

import scala.scalajs.js
import scala.scalajs.js.ConstructorTag
import scala.util.Try

package object apollo {
  type ApolloClient = ReactApolloFascade.ApolloClient
  object ApolloClient {
    // WHY INTELLIJ WHY
    def apply[O](v: O = js.undefined)(implicit ev: O => js.UndefOr[ObjectOrWritten[ApolloClientOptions]]): ApolloClient = new ApolloClient(ev(v))
  }

  // WHY INTELLIJ WHY
  def createNetworkInterface[O](options: O = js.undefined)(implicit ev: O => js.UndefOr[ObjectOrWritten[NetworkInterfaceOptions]]): NetworkInterface = {
    ReactApolloFascade.createNetworkInterface(ev(options))
  }

  def gql(query: String): Query = ReactApolloFascade.gql(query)

  def graphql[D, E](query: Query)(comp: Component)(implicit ev: ApolloQueryProps[D, E] =:= comp.Props,
                                                   constructorTag: ConstructorTag[comp.Def],
                                                   writer: Writer[D], reader: Reader[D],
                                                   extraReader: Reader[E], extraWriter: Writer[E]): DataComponent[comp.Props with ApolloProps] = {
    val dataWriter = implicitly[Writer[ApolloQueryProps[D, E]]]
    new DataComponent[comp.Props with ApolloProps](ReactApolloFascade.graphql(query, js.Dynamic.literal(
      "props" -> ((obj: js.Object) => {
        val dyn = obj.asInstanceOf[js.Dynamic]
        val networkStatus = dyn.data.networkStatus.asInstanceOf[Int]
        dataWriter.write(ApolloQueryProps[D, E](
          if (networkStatus == 1) None else Some(reader.read(dyn.data.asInstanceOf[js.Object])),
          dyn.data.loading.asInstanceOf[Boolean],
          dyn.data.error.asInstanceOf[js.Object],
          networkStatus,
          dyn.data.refetch.asInstanceOf[js.Function1[Unit, js.Promise[js.Object]]],
          extraReader.read(obj, true)
        ))
      })
    ))(comp.componentConstructor))
  }

  def graphqlMutation[D, V, E](query: Query)(comp: Component)(implicit ev: ApolloMutationProps[V, E] =:= comp.Props,
                                                              constructorTag: ConstructorTag[comp.Def],
                                                              writer: Writer[D], reader: Reader[D],
                                                              variablesWriter: Writer[V], variablesReader: Reader[V]): DataComponent[comp.Props with ApolloProps] = {
    new DataComponent[comp.Props with ApolloProps](ReactApolloFascade.graphql(query)(comp.componentConstructor))
  }

  def graphql[E](query: GraphQLQuery)(comp: Component)(implicit ev: ApolloQueryProps[query.Data, E] =:= comp.Props,
                                                       constructorTag: ConstructorTag[comp.Def],
                                                       writer: Writer[query.Data], reader: Reader[query.Data],
                                                       extraReader: Reader[E], extraWriter: Writer[E]): DataComponent[comp.Props with ApolloProps] = {
    graphql[query.Data, E](query.operation)(comp)
  }

  def graphql[E](query: GraphQLMutation)(comp: Component)(implicit ev: ApolloMutationProps[query.Variables, E] =:= comp.Props,
                                                          constructorTag: ConstructorTag[comp.Def],
                                                          writer: Writer[query.Data], reader: Reader[query.Data],
                                                          variablesWriter: Writer[query.Variables], variablesReader: Reader[query.Variables]): DataComponent[comp.Props with ApolloProps] = {
    graphqlMutation[query.Data, query.Variables, E](query.operation)(comp)
  }
}
