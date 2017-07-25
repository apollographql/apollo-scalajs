package me.shadaj.apollo

import me.shadaj.simple.react.core.{ExternalComponent, Reader, Writer}
import me.shadaj.simple.react.core.facade.ComponentInstance

import scala.scalajs.js

case class ApolloQueryProps[T, E](data: Option[T], loading: Boolean, error: js.Object, networkStatus: Int, refetch: js.Function1[Unit, js.Promise[js.Object]], extraProps: E) {
  type WithExtra[NE] = ApolloQueryProps[T, NE]
}
case class ApolloQueryPropsObj(data: Option[js.Object], loading: Boolean, error: js.Object, networkStatus: Int, refetch: js.Function1[Unit, js.Promise[js.Object]])

object ApolloQueryProps {
  private val objReader = implicitly[Reader[ApolloQueryPropsObj]]
  implicit def reader[T, E](implicit reader: Reader[T],
                            extraReader: Reader[E]): Reader[ApolloQueryProps[T, E]] = (o, root) => {
    val dataObj = objReader.read(o, root)
    ApolloQueryProps[T, E](
      dataObj.data.map(d => reader.read(d)), dataObj.loading, dataObj.error, dataObj.networkStatus, dataObj.refetch,
      extraReader.read(o, root)
    )
  }

  private val objWriter = implicitly[Writer[ApolloQueryPropsObj]]
  implicit def writer[T, E](implicit writer: Writer[T],
                            extraWriter: Writer[E]): Writer[ApolloQueryProps[T, E]] = (o, root: Boolean) => {
    val extra = extraWriter.write(o.extraProps, root)
    val normal = objWriter.write(ApolloQueryPropsObj(o.data.map(t => writer.write(t)), o.loading, o.error, o.networkStatus, o.refetch), root)

    js.Dynamic.global.Object.assign(normal, extra)

    normal
  }
}

case class ApolloMutationProps[V, E](mutate: V => js.Promise[js.Object], extraProps: E) {
  type WithExtra[NE] = ApolloMutationProps[V, NE]
}
case class ApolloMutationPropsObj(mutate: js.Object)

object ApolloMutationProps {
  private val objReader = implicitly[Reader[ApolloMutationPropsObj]]
  implicit def reader[V, E](implicit variablesWriter: Writer[V],
                            extraReader: Reader[E]): Reader[ApolloMutationProps[V, E]] = (o, root) => {
    val dataObj = objReader.read(o, root)
    ApolloMutationProps(implicitly[Reader[V => js.Promise[js.Object]]].read(dataObj.mutate), extraReader.read(o, root))
  }

  private val objWriter = implicitly[Writer[ApolloMutationPropsObj]]
  implicit def writer[V, E](implicit variablesReader: Reader[V],
                            extraWriter: Writer[E]): Writer[ApolloMutationProps[V, E]] = (o, root: Boolean) => {
    val extra = extraWriter.write(o.extraProps, root)
    val normal = objWriter.write(ApolloMutationPropsObj(
      implicitly[Writer[V => js.Promise[js.Object]]].write(o.mutate)
    ), root)

    js.Dynamic.global.Object.assign(normal, extra)

    normal
  }
}

class DataComponent[E](comp: js.Object) extends ExternalComponent {
  type Props = E

  override val component: js.Object = comp
}
