package me.shadaj.apollo

import me.shadaj.simple.react.core.fascade.ComponentInstance
import me.shadaj.simple.react.core.{Component, ExternalComponent, Writer}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("react-apollo", JSImport.Namespace)
object ReactApolloFascade extends js.Object {
  @js.native
  class ApolloClient(options: js.UndefOr[js.Dynamic] = js.undefined) extends js.Object

  def createNetworkInterface(options: js.Dynamic): NetworkInterface = js.native

  def gql(query: String): js.Object = js.native

  def graphql(query: js.Object): js.Function1[js.Any, js.Object] = js.native
}

@js.native
trait NetworkInterface extends js.Object

object ApolloProvider extends ExternalComponent {
  case class Props(client: ReactApolloFascade.ApolloClient)

  @js.native
  @JSImport("react-apollo", "ApolloProvider")
  private object Comp extends js.Object

  override val component: js.Object = Comp
}
