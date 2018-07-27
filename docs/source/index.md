---
title: Introduction
order: 0
---

Apollo Scala.js is a strongly-typed interface for using Apollo Client and React Apollo from Scala.js applications. With Apollo Scala.js, you can elegantly implement Scala components that pull data with GraphQL and use the data in a typesafe manner. Combined with Apollo CLI, you can write queries separately and automatically get static types to handle GraphQL data in your application.

Apollo Scala.js offers both a low-level interface for direct control over how GraphQL results are handled and integration with React Apollo through Slinky for embedding GraphQL in React applications. Just like Apollo Client, Apollo Scala.js also supports React Native, so you can take GraphQL wherever you go.

## Examples
For a simple application demonstrating how to perform queries and mutations with React Apollo in a Scala.js app, take a look at the [example](https://github.com/apollographql/react-apollo-scalajs/tree/master/example) folder.

## Additional Resources
+ If you're using the React API, take a look at the docs for [Slinky](https://slinky.shadaj.me), which Apollo Scala.js uses to provide access to the React Apollo's API.
+ [Apollo CLI](https://github.com/apollographql/apollo-cli) has built-in support for generating Scala types from GraphQL queries that are compatible with Apollo Scala.js
+ The official [Apollo Client docs](https://www.apollographql.com/docs/react/) provide full details on what you can do with Apollo Client, which Apollo Scala.js is built on
