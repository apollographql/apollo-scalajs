package com.apollographql.scalajs

import slinky.core.{ExternalComponent, ExternalPropsWriterProvider}
import slinky.readwrite.{Reader, Writer}

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.concurrent.Future
import scala.scalajs.js

import scala.language.implicitConversions

case class ApolloQueryOptions[Q <: GraphQLQuery](variables: Q#Variables)
case class ApolloQueryObj(variables: js.Object)
object ApolloQueryOptions {
  implicit def optionsWriter[Q <: GraphQLQuery](variablesWriter: Writer[Q#Variables]): Writer[ApolloQueryOptions[Q]] = (opts) => {
    js.Dynamic.literal(
      "variables" -> variablesWriter.write(opts.variables)
    )
  }

  implicit def fromVariables[Q <: GraphQLQuery](vars: Q#Variables): ApolloQueryOptions[Q] = ApolloQueryOptions(vars)
}

case class ApolloMutationOptions[V](variables: V)
object ApolloMutationOptions {
  implicit def optionsWriter[V](variablesWriter: Writer[V]): Writer[ApolloMutationOptions[V]] = (opts) => {
    js.Dynamic.literal(
      "variables" -> variablesWriter.write(opts.variables)
    )
  }

  implicit def fromVariables[V](vars: V): ApolloMutationOptions[V] = ApolloMutationOptions(vars)
}

case class DataResult(data: js.Object)

case class ApolloQueryProps[D, E](data: Option[D], loading: Boolean, error: js.Object, networkStatus: Int, refetch: () => Future[D], extraProps: E) {
  type WithExtra[NE] = ApolloQueryProps[D, NE]
}
case class ApolloQueryPropsObj(data: Option[js.Object], loading: Boolean, error: js.Object, networkStatus: Int, refetch: js.Object, extra: js.Object)

object ApolloQueryProps {
  private val objReader = implicitly[Reader[ApolloQueryPropsObj]]
  implicit def reader[D, E](implicit reader: Reader[D],
                            extraReader: Reader[E]): Reader[ApolloQueryProps[D, E]] = (o) => {
    val dataObj = objReader.read(o)
    val refetchData = implicitly[Reader[() => Future[DataResult]]].read(dataObj.refetch)
    ApolloQueryProps[D, E](
      dataObj.data.map(d => reader.read(d)), dataObj.loading, dataObj.error, dataObj.networkStatus,
      () => refetchData().map(d => reader.read(d.data)),
      extraReader.read(dataObj.extra)
    )
  }

  private val objWriter = implicitly[Writer[ApolloQueryPropsObj]]
  implicit def writer[D, E](implicit writer: Writer[D],
                            extraWriter: Writer[E]): Writer[ApolloQueryProps[D, E]] = (o) => {
    val extra = extraWriter.write(o.extraProps)
    objWriter.write(ApolloQueryPropsObj(
      o.data.map(t => writer.write(t)),
      o.loading, o.error, o.networkStatus,
      implicitly[Writer[() => Future[DataResult]]].write(() => o.refetch().map(d => DataResult(writer.write(d)))),
      extra
    ))
  }
}

case class ApolloMutationProps[V, D, E](mutate: ApolloMutationOptions[V] => Future[D], extraProps: E) {
  type WithExtra[NE] = ApolloMutationProps[V, D, NE]
}
case class ApolloMutationPropsObj(mutate: js.Object, extra: js.Object)

object ApolloMutationProps {
  private val objReader = implicitly[Reader[ApolloMutationPropsObj]]
  implicit def reader[V, D, E](implicit variablesWriter: Writer[V],
                               extraReader: Reader[E],
                               dataReader: Reader[D]): Reader[ApolloMutationProps[V, D, E]] = (o) => {
    val dataObj = objReader.read(o)
    val dataMutate = implicitly[Reader[ApolloMutationOptions[V] => Future[DataResult]]].read(dataObj.mutate)
    ApolloMutationProps(
      v => dataMutate(v).map(d => dataReader.read(d.data)),
      extraReader.read(dataObj.extra)
    )
  }

  private val objWriter = implicitly[Writer[ApolloMutationPropsObj]]
  implicit def writer[V, D, E](implicit variablesReader: Reader[V],
                               extraWriter: Writer[E],
                               dataWriter: Writer[D]): Writer[ApolloMutationProps[V, D, E]] = (o) => {
    val extra = extraWriter.write(o.extraProps)
    objWriter.write(ApolloMutationPropsObj(
      implicitly[Writer[ApolloMutationOptions[V] => Future[DataResult]]].write(v => o.mutate(v).map(d => DataResult(dataWriter.write(d)))),
      extra
    ))
  }
}

class DataComponent[E](comp: js.Object)(implicit extraPropsWriter: Writer[E])
  extends ExternalComponent()(extraPropsWriter.asInstanceOf[ExternalPropsWriterProvider]) {
  type Props = E

  override val component = comp
}
