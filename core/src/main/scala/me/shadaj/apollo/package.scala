package me.shadaj

import me.shadaj.slinky.core.{Component, ObjectOrWritten, Reader, Writer}

import scala.scalajs.js
import scala.scalajs.js.{ConstructorTag, JSON}
import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

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

  def graphql[D, V, E](query: Query, variables: E => Option[V])
                      (comp: Component)
                      (implicit ev: ApolloQueryProps[D, E] =:= comp.Props,
                       constructorTag: ConstructorTag[comp.Def],
                       writer: Writer[D], reader: Reader[D],
                       variablesWriter: Writer[V], variablesReader: Reader[V],
                       extraReader: Reader[E], extraWriter: Writer[E]): DataComponent[comp.Props with ApolloProps] = {
    val dataWriter = implicitly[Writer[ApolloQueryProps[D, E]]]

    new DataComponent[comp.Props with ApolloProps](ReactApolloFascade.graphql(query, js.Dynamic.literal(
      "props" -> ((obj: js.Object) => {
        val dyn = obj.asInstanceOf[js.Dynamic]
        val networkStatus = dyn.data.networkStatus.asInstanceOf[Int]
        val refetchData = implicitly[Reader[Unit => Future[DataResult]]].read(dyn.data.refetch.asInstanceOf[js.Object])
        dataWriter.write(ApolloQueryProps[D, E](
          if (networkStatus == 1) None else Some(reader.read(dyn.data.asInstanceOf[js.Object])),
          dyn.data.loading.asInstanceOf[Boolean],
          dyn.data.error.asInstanceOf[js.Object],
          networkStatus,
          _ => refetchData().map(d => reader.read(d.data)),
          extraReader.read(dyn.ownProps.asInstanceOf[js.Object], true)
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

  def graphqlMutation[D, V, E](query: Query)(comp: Component)(implicit ev: ApolloMutationProps[V, D, E] =:= comp.Props,
                                                              constructorTag: ConstructorTag[comp.Def],
                                                              writer: Writer[D], reader: Reader[D],
                                                              variablesWriter: Writer[V], variablesReader: Reader[V]): DataComponent[comp.Props with ApolloProps] = {
    new DataComponent[comp.Props with ApolloProps](ReactApolloFascade.graphql(query)(comp.componentConstructor))
  }

  def graphql[E](query: GraphQLQuery)
                (comp: Component)
                (implicit ev: ApolloQueryProps[query.Data, E] =:= comp.Props,
                 constructorTag: ConstructorTag[comp.Def],
                 writer: Writer[query.Data], reader: Reader[query.Data],
                 variablesWriter: Writer[query.Variables], variablesReader: Reader[query.Variables],
                 extraReader: Reader[E], extraWriter: Writer[E]): DataComponent[comp.Props with ApolloProps] = {
    graphql[query.Data, query.Variables, E](query.operation, _ => None)(comp)
  }

  def graphqlWithVariables[E](query: GraphQLQuery)
                             (variables: E => Option[query.Variables])(comp: Component)
                (implicit ev: ApolloQueryProps[query.Data, E] =:= comp.Props,
                 constructorTag: ConstructorTag[comp.Def],
                 writer: Writer[query.Data], reader: Reader[query.Data],
                 variablesWriter: Writer[query.Variables], variablesReader: Reader[query.Variables],
                 extraReader: Reader[E], extraWriter: Writer[E]): DataComponent[comp.Props with ApolloProps] = {
    graphql[query.Data, query.Variables, E](query.operation, variables)(comp)
  }

  def graphql[E](query: GraphQLMutation)(comp: Component)(implicit ev: ApolloMutationProps[query.Variables, query.Data, E] =:= comp.Props,
                                                          constructorTag: ConstructorTag[comp.Def],
                                                          writer: Writer[query.Data], reader: Reader[query.Data],
                                                          variablesWriter: Writer[query.Variables], variablesReader: Reader[query.Variables]): DataComponent[comp.Props with ApolloProps] = {
    graphqlMutation[query.Data, query.Variables, E](query.operation)(comp)
  }
}
