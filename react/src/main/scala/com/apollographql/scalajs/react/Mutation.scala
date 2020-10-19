package com.apollographql.scalajs.react

import com.apollographql.scalajs._
import slinky.core.ExternalComponent
import slinky.core.facade.ReactElement
import slinky.readwrite.{Reader, Writer}

import scala.concurrent.Future
import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.|
import scala.scalajs.js.annotation.JSImport

case class MutationData[T](loading: Boolean, called: Boolean, error: Option[js.Error], data: Option[T])
object MutationData {
  implicit def reader[T](implicit tReader: Reader[T]): Reader[MutationData[T]] = { o =>
    val dyn = o.asInstanceOf[js.Dynamic]
    val loading = Reader.booleanReader.read(dyn.loading.asInstanceOf[js.Object])
    MutationData(
      loading,
      Reader.booleanReader.read(dyn.called.asInstanceOf[js.Object]),
      Reader.optionReader[js.Error].read(dyn.error.asInstanceOf[js.Object]),
      if (loading) None else Reader.optionReader(tReader).read(dyn.data.asInstanceOf[js.Object])
    )
  }
}

case class CallMutationProps[V](variables: V)
object CallMutationProps {
  implicit def vToCall[V](value: V): CallMutationProps[V] = CallMutationProps(value)
  implicit def writer[T](implicit tWriter: Writer[T]): Writer[CallMutationProps[T]] = { v =>
    js.Dynamic.literal(
      variables = tWriter.write(v.variables)
    )
  }
}

case class UpdateStrategy(refetchQueries: Seq[String] = Seq())
object Mutation extends ExternalComponent {
  case class Props(mutation: DocumentNode,
                   refetchQueries: Seq[String],
                   children: (js.Object, js.Object) => ReactElement)

  def apply[Res](query: DocumentNode)
              (children: (CallMutationProps[Unit] => Future[MutationResult[Res]], MutationData[Res]) => ReactElement)
              (implicit tReader: Reader[Res]): slinky.core.BuildingComponent[Element, js.Object] = {
    apply(query, UpdateStrategy())(children)
  }

  type DynamicMutationAsyncCallback[Var, Res] = CallMutationProps[Var] => Future[MutationResult[Res]]

  def apply[Res, Var](query: DocumentNode)
                 (children: (DynamicMutationAsyncCallback[Var, Res], MutationData[Res]) => ReactElement)
                 (implicit tReader: Reader[Res], vWriter: Writer[Var]): slinky.core.BuildingComponent[Element, js.Object] = {
    apply(query, UpdateStrategy())(children)
  }

  def apply[Res, Var](query: DocumentNode, updateStrategy: UpdateStrategy)
                 (children: (DynamicMutationAsyncCallback[Var, Res], MutationData[Res]) => ReactElement)
                 (implicit tReader: Reader[Res], vWriter: Writer[Var]): slinky.core.BuildingComponent[Element, js.Object] = {
    val queryDataReader = MutationData.reader(tReader)
    apply(Props(
      mutation = query,
      refetchQueries = updateStrategy.refetchQueries,
      children = (call, d) => {
        children(
          implicitly[Reader[CallMutationProps[Var] => Future[MutationResult[Res]]]].read(call),
          queryDataReader.read(d)
        )
      }
    ))
  }
  
  type StaticMutationAsyncCallback[Q <: GraphQLMutation] = CallMutationProps[Q#Variables] => Future[MutationResult[Q#Data]]

  def apply[Q <: GraphQLMutation](query: Q)
                                 (children: (StaticMutationAsyncCallback[Q], MutationData[Q#Data]) => ReactElement)
                                 (implicit dataReader: Reader[Q#Data],
                                  variablesWriter: Writer[Q#Variables]): slinky.core.BuildingComponent[Element, js.Object] = {
    apply(query: Q, UpdateStrategy())(children)
  }

  def apply[Q <: GraphQLMutation](query: Q, updateStrategy: UpdateStrategy)
                                 (children: (StaticMutationAsyncCallback[Q], MutationData[Q#Data]) => ReactElement)
                                 (implicit dataReader: Reader[Q#Data],
                                  variablesWriter: Writer[Q#Variables]): slinky.core.BuildingComponent[Element, js.Object] = {
    val queryDataReader = MutationData.reader(dataReader)
    apply(Props(
      mutation = query.operation,
      refetchQueries = updateStrategy.refetchQueries,
      children = (call, d) => {
        children(
          implicitly[Reader[CallMutationProps[Q#Variables] => Future[MutationResult[Q#Data]]]].read(call),
          queryDataReader.read(d)
        )
      }
    ))
  }

  @js.native
  @JSImport("@apollo/client/react/components", "Mutation")
  object MutationComponent extends js.Object

  override val component = MutationComponent
}
