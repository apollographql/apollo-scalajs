package com.apollographql.scalajs.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import com.apollographql.scalajs._

@JSImport("@apollo/client", JSImport.Namespace)
@js.native
object HooksApi extends js.Object {
  def useQuery[Q <: GraphQLQuery](
    query: DocumentNode,
    props: js.UndefOr[QueryHookOptions[Q]] = js.undefined
  ): QueryResult[Q] = js.native

  def useMutation[Q <: GraphQLMutation](
    query: DocumentNode,
    props: js.UndefOr[MutationHookOptions[Q]] = js.undefined
  ): js.Tuple2[js.Function1[js.UndefOr[MutationHookOptions[Q]], js.Promise[MutationResult[Q#Data]]], MutationResult[Q#Data]] = js.native
}

trait QueryHookOptions[Q <: GraphQLQuery] extends js.Object {
  val variables: js.UndefOr[Q#Variables] = js.undefined
  val pollInterval: js.UndefOr[Int] = js.undefined
  val notifyOnNetworkStatusChange: js.UndefOr[Boolean] = js.undefined
  val fetchPolicy: js.UndefOr[FetchPolicy] = js.undefined
  val errorPolicy: js.UndefOr[ErrorPolicy] = js.undefined
  val ssr: js.UndefOr[Boolean] = js.undefined
  val displayName: js.UndefOr[String] = js.undefined
  val skip: js.UndefOr[Boolean] = js.undefined
  val partialRefetch: js.UndefOr[Boolean] = js.undefined
  val client: js.UndefOr[ApolloClient] = js.undefined
  val returnPartialData: js.UndefOr[Boolean] = js.undefined
}

object QueryHookOptions {
  def apply[Q <: GraphQLQuery](
  variables: js.UndefOr[Q#Variables] = js.undefined,
  pollInterval: js.UndefOr[Int] = js.undefined,
  notifyOnNetworkStatusChange: js.UndefOr[Boolean] = js.undefined,
  fetchPolicy: js.UndefOr[FetchPolicy] = js.undefined,
  errorPolicy: js.UndefOr[ErrorPolicy] = js.undefined,
  ssr: js.UndefOr[Boolean] = js.undefined,
  displayName: js.UndefOr[String] = js.undefined,
  skip: js.UndefOr[Boolean] = js.undefined,
  partialRefetch: js.UndefOr[Boolean] = js.undefined,
  client: js.UndefOr[ApolloClient] = js.undefined,
  returnPartialData: js.UndefOr[Boolean] = js.undefined
): QueryHookOptions[Q] = {
    js.Dynamic
      .literal(
        variables = variables.asInstanceOf[js.Object],
        pollInterval = pollInterval,
        notifyOnNetworkStatusChange = notifyOnNetworkStatusChange,
        fetchPolicy = fetchPolicy,
        errorPolicy = errorPolicy,
        ssr = ssr,
        displayName = displayName,
        skip = skip,
        partialRefetch = partialRefetch,
        client = client,
        returnPartialData = returnPartialData
      )
      .asInstanceOf[QueryHookOptions[Q]]
  }
}

trait FetchPolicy extends js.Object

object FetchPolicy {
  val CacheFirst: FetchPolicy = "cache-first".asInstanceOf[FetchPolicy]
  val NetworkOnly: FetchPolicy = "network-only".asInstanceOf[FetchPolicy]
  val CacheOnly: FetchPolicy = "cache-only".asInstanceOf[FetchPolicy]
  val NoCache: FetchPolicy = "no-cache".asInstanceOf[FetchPolicy]
  val Standby: FetchPolicy = "standby".asInstanceOf[FetchPolicy]
  val CacheAndNetwork: FetchPolicy = "cache-and-network".asInstanceOf[FetchPolicy]
}


trait ErrorPolicy extends js.Object

object ErrorPolicy {
  val None: ErrorPolicy = "none".asInstanceOf[ErrorPolicy]
  val Ignore: ErrorPolicy = "ignore".asInstanceOf[ErrorPolicy]
  val All: ErrorPolicy = "all".asInstanceOf[ErrorPolicy]
}

@js.native
trait QueryResult[Q <: GraphQLQuery] extends js.Object {
  val data: js.UndefOr[Q#Data] = js.native
  val loading: Boolean = js.native
  val error: js.UndefOr[js.Error] = js.native
  val variables: js.UndefOr[Q#Variables] = js.native
  val networkStatus: Int = js.native
  val refetch: js.Function1[Q#Variables, js.Promise[ApolloQueryResult[Q#Data]]] = js.native
  val fetchMore: js.Function3[DocumentNode, Q#Variables, js.Function, js.Promise[ApolloQueryResult[Q#Data]]] = js.native
  val startPolling: js.Function1[Int, Unit] = js.native
  val subscribeToMore: js.Function1[js.Dynamic, js.Function0[Unit]] = js.native
  val updateQuery: js.Function2[Q#Data, js.Dynamic, Q#Data] = js.native
  val client: ApolloClient = js.native
  val called: Boolean = js.native
}

object QueryResult {
  // An unapply for the common use case to extract values
  def unapply[Q <: GraphQLQuery](result: QueryResult[Q]): Option[(js.UndefOr[Q#Data], js.UndefOr[js.Error], Boolean)] = {
    Some((result.data, result.error, result.loading))
  }
}

@js.native
trait MutationHookOptions[Q <: GraphQLMutation] extends js.Object {
  val variables: js.UndefOr[Q#Variables] = js.undefined
  val refetchQueries: js.UndefOr[js.Array[js.Dynamic]] = js.undefined
}

object MutationHookOptions {
  def apply[Q <: GraphQLMutation](
    variables: js.UndefOr[Q#Variables] = js.undefined,
    refetchQueries: js.UndefOr[js.Array[js.Dynamic]] = js.undefined
  ): MutationHookOptions[Q] = {
    js.Dynamic
      .literal(
        variables = variables.asInstanceOf[js.Object],
        refetchQueries = refetchQueries
      )
      .asInstanceOf[MutationHookOptions[Q]]
  }
}

@js.native
trait MutationResult[D] extends js.Object {
  val data: js.UndefOr[D] = js.native
  val loading: Boolean = js.native
  val error: js.UndefOr[js.Error] = js.native
  val called: Boolean = js.native
  val client: ApolloClient = js.native
}
