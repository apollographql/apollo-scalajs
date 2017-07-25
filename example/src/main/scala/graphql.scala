//  This file was automatically generated and should not be edited.


package me.shadaj.apollo

object AllPostsQuery extends me.shadaj.apollo.GraphQLQuery {
  val operationString =
    "query AllPosts {" +
    "  posts {" +
    "    id" +
    "    title" +
    "    votes" +
    "  }" +
    "}"
  val operation = me.shadaj.apollo.gql(operationString)


  case class Data(posts: Option[Seq[Option[Post]]]) {
  }

  object Data {
    val possibleTypes = scala.collection.Set("Query")
  }

  case class Post(id: Int, title: Option[String], votes: Option[Int]) {
  }

  object Post {
    val possibleTypes = scala.collection.Set("Post")
  }
}

object UpVoteMutation extends GraphQLMutation {
  val operationString =
    "mutation UpVote {" +
    "  upvotePost(postId: 1) {" +
    "    id" +
    "    title" +
    "    votes" +
    "  }" +
    "}"
  val operation = me.shadaj.apollo.gql(operationString)


  case class Data(upvotePost: Option[UpvotePost]) {
  }

  object Data {
    val possibleTypes = scala.collection.Set("Mutation")
  }

  case class UpvotePost(id: Int, title: Option[String], votes: Option[Int]) {
  }

  object UpvotePost {
    val possibleTypes = scala.collection.Set("Post")
  }
}