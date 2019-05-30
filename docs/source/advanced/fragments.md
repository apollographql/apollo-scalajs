---
title: Using Fragments
description: ''
---

Fragments are a powerful tool in statically-typed languages, allowing you to share a common model type across multiple queries in your application. Apollo Scala.js handles fragment types through implicit conversions between the original data type generated for a specific query and the fragment type that contains a specific subset of fields.

## Generating types for fragments
The Apollo CLI handles fragments just like any other GraphQL definition. Because all your GraphQL files are merged before code generation runs, fragments can be defined in their own file and referenced from other queries in your project.

Let's take a look at the code generation output for a simple query that references a fragment:

```graphql
fragment CurrencyFragment on ExchangeRate {
  currency
}

query USDRatesWithFragment {
  rates(currency: "USD") {
    ...CurrencyFragment
  }
}
```

This will result in the following type definitions, with a query-specific type `UsdRatesWithFragmentQuery.Data.Rate` but also a general fragment type `CurrencyFragment`:

```scala
object UsdRatesWithFragmentQuery extends com.apollographql.scalajs.GraphQLQuery {
  // ...

  case class Data(rates: Option[Seq[Option[Data.Rate]]]) {
  }

  object Data {
    val possibleTypes = scala.collection.Set("Query")

    case class Rate(currency: Option[String]) {
    }

    object Rate {
      val possibleTypes = scala.collection.Set("ExchangeRate")
      implicit def toCurrencyFragment(a: Rate): CurrencyFragment = CurrencyFragment(a.currency)
    }
  }
}

case class CurrencyFragment(currency: Option[String]) {
}
```

## Applying fragments in application code

If we have components of our application that will be recieving currency data from different queries, we could then use the `CurrencyFragment` type to recieve that data.

```scala
def myGeneralCurrencyCode(currencyData: CurrencyFragment) =
  currencyData.currency.getOrElse("Currency not known")

// ...

Query(UsdRatesWithFragmentQuery) { d =>
  if (d.data.isDefined) {
    div({
      for {
        rates <- d.data.get.rates // Seq[Option[Data.Rate]]
        maybeRate <- rates // Option[Data.Rate]
        rate <- maybeRate // Data.Rate
      } yield div(myGeneralCurrencyCode(rate /* converted to CurrencyFragment here */))
    })
    gotDataPromise.success(assert(d.data.get.rates.exists(_.nonEmpty)))
  } else {
    div("Loading!")
  }
}
```
