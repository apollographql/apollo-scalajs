package me.shadaj

import me.shadaj.simple.react.core.{Component, ObjectOrWritten, Reader, Writer}

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

  def graphql[P](query: Query)(comp: Component)(implicit ev: ApolloData[P] =:= comp.Props,
                                                constructorTag: ConstructorTag[comp.Def],
                                                writer: Writer[comp.Props], reader: Reader[P]): DataComponent = {
    new DataComponent(ReactApolloFascade.graphql(query, js.Dynamic.literal(
      "props" -> ((obj: js.Object) => {
        val dyn = obj.asInstanceOf[js.Dynamic]
        writer.write(ApolloData[P](
          Try(reader.read(dyn.data.asInstanceOf[js.Object])).toOption,
          dyn.data.loading.asInstanceOf[Boolean],
          dyn.data.error.asInstanceOf[js.Object],
          dyn.data.networkStatus.asInstanceOf[Int]
        ))
      })
    ))(comp.componentReference))
  }
}
