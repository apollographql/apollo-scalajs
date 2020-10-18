package com.apollographql.scalajs.react

import com.apollographql.scalajs.{GraphQLQuery, DocumentNode}
import slinky.core.ExternalComponent
import slinky.core.facade.ReactElement
import slinky.readwrite.{Reader, Writer}

import scala.util.Try
import scala.scalajs.js
import scala.scalajs.js.|
import scala.scalajs.js.annotation.JSImport

case class QueryData[T](loading: Boolean, error: Option[js.Error], data: Option[T], refetch: () => Unit)
object QueryData {
  implicit def reader[T](implicit tReader: Reader[T]): Reader[QueryData[T]] = { o =>
    val dyn = o.asInstanceOf[js.Dynamic]
    val loading = Reader.booleanReader.read(dyn.loading.asInstanceOf[js.Object])
    val error = Reader.optionReader[js.Error].read(dyn.error.asInstanceOf[js.Object])
    QueryData(
      loading,
      error,
      if (!js.isUndefined(dyn.data) && js.Object.keys(dyn.data.asInstanceOf[js.Object]).nonEmpty) {
        Some(tReader.read(dyn.data.asInstanceOf[js.Object]))
      } else None,
      implicitly[Reader[() => Unit]].read(dyn.refetch.asInstanceOf[js.Object])
    )
  }
}

case class ExtraQueryOptions(pollInterval: js.UndefOr[Double] = js.undefined,
                             notifyOnNetworkStatusChange: js.UndefOr[Boolean] = js.undefined,
                             fetchPolicy: js.UndefOr[String] = js.undefined,
                             errorPolicy: js.UndefOr[String] = js.undefined,
                             ssr: js.UndefOr[Boolean] = js.undefined,
                             displayName: js.UndefOr[String] = js.undefined,
                             delay: js.UndefOr[Boolean] = js.undefined,
                             context: js.UndefOr[js.Object] = js.undefined)

object Query extends ExternalComponent {
  case class Props(query: DocumentNode,
                   children: js.Object => ReactElement,
                   variables: js.UndefOr[js.Object] = js.undefined,
                   pollInterval: js.UndefOr[Double] = js.undefined,
                   notifyOnNetworkStatusChange: js.UndefOr[Boolean] = js.undefined,
                   fetchPolicy: js.UndefOr[String] = js.undefined,
                   errorPolicy: js.UndefOr[String] = js.undefined,
                   ssr: js.UndefOr[Boolean] = js.undefined,
                   displayName: js.UndefOr[String] = js.undefined,
                   delay: js.UndefOr[Boolean] = js.undefined,
                   context: js.UndefOr[js.Object] = js.undefined)
  def apply[T, V](query: DocumentNode, variables: V, queryOptions: ExtraQueryOptions)
                 (children: QueryData[T] => ReactElement)
                 (implicit tReader: Reader[T], vWriter: Writer[V]): slinky.core.BuildingComponent[Element, js.Object] = {
    val queryDataReader = QueryData.reader(tReader)
    apply(Props(
      query = query,
      variables = vWriter.write(variables),
      children = d => {
        children(queryDataReader.read(d))
      },
      // queryOptions
      pollInterval = queryOptions.pollInterval,
      notifyOnNetworkStatusChange = queryOptions.notifyOnNetworkStatusChange,
      fetchPolicy = queryOptions.fetchPolicy,
      errorPolicy = queryOptions.errorPolicy,
      ssr = queryOptions.ssr,
      displayName = queryOptions.displayName,
      delay = queryOptions.delay,
      context = queryOptions.context
    ))
  }

  def apply[T, V](query: DocumentNode, variables: V)
                 (children: QueryData[T] => ReactElement)
                 (implicit tReader: Reader[T], vWriter: Writer[V]): slinky.core.BuildingComponent[Element, js.Object] = {
    apply[T, V](
      query = query,
      variables = variables,
      queryOptions = ExtraQueryOptions()
    )(children)
  }

  def apply[T](query: DocumentNode, queryOptions: ExtraQueryOptions)
              (children: QueryData[T] => ReactElement)
              (implicit tReader: Reader[T]): slinky.core.BuildingComponent[Element, js.Object] = {
    apply[T, Unit](
      query = query,
      (),
      queryOptions
    )(children)
  }

  def apply[T](query: DocumentNode)
              (children: QueryData[T] => ReactElement)
              (implicit tReader: Reader[T]): slinky.core.BuildingComponent[Element, js.Object] = {
    apply[T](
      query = query,
      queryOptions = ExtraQueryOptions()
    )(children)
  }

  def apply[Q <: GraphQLQuery](query: Q, variables: Q#Variables, queryOptions: ExtraQueryOptions)
                              (children: QueryData[query.Data] => ReactElement)
                              (implicit dataReader: Reader[query.Data],
                               variablesWriter: Writer[Q#Variables]): slinky.core.BuildingComponent[Element, js.Object] = {
    apply[query.Data, Q#Variables](
      query = query.operation,
      variables = variables,
      queryOptions = queryOptions
    )(children)
  }

  def apply[Q <: GraphQLQuery](query: Q, variables: Q#Variables)
                              (children: QueryData[query.Data] => ReactElement)
                              (implicit dataReader: Reader[query.Data],
                               variablesWriter: Writer[Q#Variables]): slinky.core.BuildingComponent[Element, js.Object] = {
    apply[Q](
      query = query,
      variables = variables,
      queryOptions = ExtraQueryOptions()
    )(children)
  }

  def apply(query: GraphQLQuery { type Variables = Unit }, queryOptions: ExtraQueryOptions)
           (children: QueryData[query.Data] => ReactElement)
           (implicit dataReader: Reader[query.Data]): slinky.core.BuildingComponent[Element, js.Object] = {
    apply[GraphQLQuery {type Variables = Unit }](
      query = query,
      variables = (),
      queryOptions = queryOptions
    )(children)
  }

  def apply(query: GraphQLQuery { type Variables = Unit })
           (children: QueryData[query.Data] => ReactElement)
           (implicit dataReader: Reader[query.Data]): slinky.core.BuildingComponent[Element, js.Object] = {
    apply(
      query = query,
      queryOptions = ExtraQueryOptions()
    )(children)
  }

  @js.native
  @JSImport("@apollo/client/react/components", "Query")
  object QueryComponent extends js.Object

  override val component = QueryComponent
}
