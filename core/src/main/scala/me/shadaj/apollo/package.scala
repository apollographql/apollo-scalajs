package me.shadaj

import me.shadaj.simple.react.core.{Component, Reader, Writer}
import me.shadaj.simple.react.core.fascade.ComponentInstance

import scala.scalajs.js
import scala.scalajs.js.ConstructorTag

package object apollo {
  type ApolloClient = ReactApolloFascade.ApolloClient

  def createNetworkInterface(options: js.Dynamic): NetworkInterface = ReactApolloFascade.createNetworkInterface(options)

  def gql(query: String): js.Object = ReactApolloFascade.gql(query)

  private def graphqlFunction(query: js.Object)(fn: js.Function1[js.Object, ComponentInstance]): DataComponent = {
    new DataComponent(ReactApolloFascade.graphql(query)(fn))
  }

  def graphql[P](query: js.Object)(comp: Component)(implicit ev: ApolloData[P] =:= comp.Props, constructorTag: ConstructorTag[comp.Def], writer: Writer[comp.Props], reader: Reader[P]): DataComponent = {
    graphqlFunction(query)(obj => {
      val dyn = obj.asInstanceOf[js.Dynamic]
      val networkStatus = dyn.data.networkStatus.asInstanceOf[Int]
      comp(ApolloData[P](
        if (networkStatus >= 7) Some(reader.read(dyn.data.asInstanceOf[js.Object])) else None,
        dyn.data.loading.asInstanceOf[Boolean],
        dyn.data.error.asInstanceOf[js.Object],
        networkStatus
      ))
    })
  }
}
