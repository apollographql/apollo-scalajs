package me.shadaj.apollo

import me.shadaj.simple.react.core.{ExternalComponent, Reader, Writer}
import me.shadaj.simple.react.core.fascade.ComponentInstance

import scala.scalajs.js

case class ApolloData[T](data: Option[T], loading: Boolean, error: js.Object, networkStatus: Int)

case class ApolloDataObj(data: Option[js.Object], loading: Boolean, error: js.Object, networkStatus: Int)

object ApolloData {
  private val objReader = implicitly[Reader[ApolloDataObj]]
  private val objWriter = implicitly[Writer[ApolloDataObj]]

  implicit def reader[T](implicit reader: Reader[T]): Reader[ApolloData[T]] = new Reader[ApolloData[T]] {
    override def read(o: js.Object, root: Boolean): ApolloData[T] = {
      val dataObj = objReader.read(o, root)
      ApolloData[T](dataObj.data.map(d => reader.read(d)), dataObj.loading, dataObj.error, dataObj.networkStatus)
    }
  }

  implicit def writer[T](implicit writer: Writer[T]): Writer[ApolloData[T]] = new Writer[ApolloData[T]] {
    override def write(o: ApolloData[T], root: Boolean): js.Object = {
      objWriter.write(ApolloDataObj(o.data.map(t => writer.write(t)), o.loading, o.error, o.networkStatus))
    }
  }
}

class DataComponent(comp: js.Object) extends ExternalComponent {
  type Props = Unit

  override val component: js.Object = comp

  def apply(): ComponentInstance = this.apply(())
}
