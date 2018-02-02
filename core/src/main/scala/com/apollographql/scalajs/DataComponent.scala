package com.apollographql.scalajs

import slinky.core.ExternalComponent
import slinky.readwrite.{Reader, Writer}

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.concurrent.Future
import scala.scalajs.js

case class DataResult(data: js.Object)

trait ApolloProps {
  type ExtraProps
}

case class ApolloQueryProps[D, E](data: Option[D], loading: Boolean, error: js.Object, networkStatus: Int, refetch: () => Future[D], extraProps: E) extends ApolloProps {
  override type ExtraProps = E
  type WithExtra[NE] = ApolloQueryProps[D, NE]
}
case class ApolloQueryPropsObj(data: Option[js.Object], loading: Boolean, error: js.Object, networkStatus: Int, refetch: js.Object)

object ApolloQueryProps {
  private val objReader = implicitly[Reader[ApolloQueryPropsObj]]
  implicit def reader[D, E](implicit reader: Reader[D],
                            extraReader: Reader[E]): Reader[ApolloQueryProps[D, E]] = (o) => {
    val dataObj = objReader.read(o)
    val refetchData = implicitly[Reader[() => Future[DataResult]]].read(dataObj.refetch)
    ApolloQueryProps[D, E](
      dataObj.data.map(d => reader.read(d)), dataObj.loading, dataObj.error, dataObj.networkStatus,
      () => refetchData().map(d => reader.read(d.data)),
      extraReader.read(o)
    )
  }

  private val objWriter = implicitly[Writer[ApolloQueryPropsObj]]
  implicit def writer[D, E](implicit writer: Writer[D],
                            extraWriter: Writer[E]): Writer[ApolloQueryProps[D, E]] = (o) => {
    val extra = extraWriter.write(o.extraProps)
    val normal = objWriter.write(ApolloQueryPropsObj(
      o.data.map(t => writer.write(t)),
      o.loading, o.error, o.networkStatus,
      implicitly[Writer[() => Future[DataResult]]].write(() => o.refetch().map(d => DataResult(writer.write(d))))
    ))

    js.Dynamic.global.Object.assign(normal, extra)

    normal
  }
}

case class ApolloMutationProps[V, D, E](mutate: V => Future[D], extraProps: E) extends ApolloProps {
  override type ExtraProps = E
  type WithExtra[NE] = ApolloMutationProps[V, D, NE]
}
case class ApolloMutationPropsObj(mutate: js.Object)

object ApolloMutationProps {
  private val objReader = implicitly[Reader[ApolloMutationPropsObj]]
  implicit def reader[V, D, E](implicit variablesWriter: Writer[V],
                               extraReader: Reader[E],
                               dataReader: Reader[D]): Reader[ApolloMutationProps[V, D, E]] = (o) => {
    val dataObj = objReader.read(o)
    val dataMutate = implicitly[Reader[V => Future[DataResult]]].read(dataObj.mutate)
    ApolloMutationProps(
      v => dataMutate(v).map(d => dataReader.read(d.data)),
      extraReader.read(o)
    )
  }

  private val objWriter = implicitly[Writer[ApolloMutationPropsObj]]
  implicit def writer[V, D, E](implicit variablesReader: Reader[V],
                               extraWriter: Writer[E],
                               dataWriter: Writer[D]): Writer[ApolloMutationProps[V, D, E]] = (o) => {
    val extra = extraWriter.write(o.extraProps)
    val normal = objWriter.write(ApolloMutationPropsObj(
      implicitly[Writer[V => Future[DataResult]]].write(v => o.mutate(v).map(d => DataResult(dataWriter.write(d))))
    ))

    js.Dynamic.global.Object.assign(normal, extra)

    normal
  }
}

class DataComponent[P <: ApolloProps](comp: js.Object) extends ExternalComponent {
  type Props = P#ExtraProps

  override val component = comp
}
