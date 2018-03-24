package com.apollographql.scalajs

import slinky.core.ExternalComponent
import slinky.core.facade.ReactElement
import slinky.readwrite.{Reader, Writer}

import scala.scalajs.js
import scala.scalajs.js.|

case class QueryData[T](loading: Boolean, error: Option[Error], data: Option[T], refetch: () => Unit)
object QueryData {
  implicit def reader[T](implicit tReader: Reader[T]): Reader[QueryData[T]] = { o =>
    val dyn = o.asInstanceOf[js.Dynamic]
    val loading = Reader.booleanReader.read(dyn.loading.asInstanceOf[js.Object])
    QueryData(
      loading,
      Reader.optionReader[Error].read(dyn.error.asInstanceOf[js.Object]),
      if (loading) None else Some(tReader.read(dyn.data.asInstanceOf[js.Object])),
      implicitly[Reader[() => Unit]].read(dyn.refetch.asInstanceOf[js.Object])
    )
  }
}

object Query extends ExternalComponent {
  case class Props(query: ParsedQuery,
                   variables: js.UndefOr[js.Object] = js.undefined,
                   children: js.Object => ReactElement)

  def apply[T](query: ParsedQuery)
              (children: QueryData[T] => ReactElement)
              (implicit tReader: Reader[T]): slinky.core.BuildingComponent[Element, js.Object] = {
    val queryDataReader = QueryData.reader(tReader)
    apply(Props(
      query = query,
      children = d => {
        children(queryDataReader.read(d))
      }
    ))
  }

  def apply[T, V](query: ParsedQuery, variables: V)
                 (children: QueryData[T] => ReactElement)
                 (implicit tReader: Reader[T], vWriter: Writer[V]): slinky.core.BuildingComponent[Element, js.Object] = {
    val queryDataReader = QueryData.reader(tReader)
    apply(Props(
      query = query,
      variables = vWriter.write(variables),
      children = d => {
        children(queryDataReader.read(d))
      }
    ))
  }

  def apply(query: GraphQLQuery { type Variables = Unit })
           (children: QueryData[query.Data] => ReactElement)
           (implicit dataReader: Reader[query.Data]): slinky.core.BuildingComponent[Element, js.Object] = {
    val queryDataReader = QueryData.reader(dataReader)
    apply(Props(
      query = query.operation,
      children = d => {
        children(queryDataReader.read(d))
      }
    ))
  }

  def apply[Q <: GraphQLQuery](query: Q, variables: Q#Variables)
                              (children: QueryData[query.Data] => ReactElement)
                              (implicit dataReader: Reader[query.Data],
                               variablesWriter: Writer[Q#Variables]): slinky.core.BuildingComponent[Element, js.Object] = {
    val queryDataReader = QueryData.reader(dataReader)
    apply(Props(
      query = query.operation,
      variables = variablesWriter.write(variables),
      children = d => {
        children(queryDataReader.read(d))
      }
    ))
  }

  override val component: |[String, js.Object] = ReactApollo.Query
}
