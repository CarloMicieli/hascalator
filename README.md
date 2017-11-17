![Logo](img/logo.png)  
(Logo design by *Sandro Stucky* - retrivied from the Martin Odersky's talk _The Evolution of Scala_, 2015).

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build Status](https://travis-ci.org/CarloMicieli/hascalator.png?branch=master)](https://travis-ci.org/CarloMicieli/hascalator)
[![Coverage Status](https://coveralls.io/repos/github/CarloMicieli/hascalator/badge.svg?branch=master)](https://coveralls.io/github/CarloMicieli/hascalator?branch=master)
[![Dependencies](https://app.updateimpact.com/badge/763721648812724224/hascalator.svg?config=compile)](https://app.updateimpact.com/latest/763721648812724224/hascalator)

This library implements the following data types:

* `data.Dequeue`
* `data.Either`
* `data.Heap`
* `data.List`
* `data.NonEmpty`
* `data.Maybe`
* `data.Queue`
* `data.Ratio`
* `data.Rational`
* `data.Set`
* `data.Stack`
* `data.Tuple`

and the following typeclasses:

* `Bounded`
* `Enum`
* `Eq`
* `Fractional`
* `Integral`
* `Num`
* `Ord`
* `Real`
* `RealFrac`
* `Show`
* `Semigroup`
* `Monoid`

## Layout ##

The project has the following layout:

```
+ bench... JMH benchmarks
+ core.... core sources
+ docs.... tutorial
```

```
+ src
--+ main
----+ scala
------+ io
---------+ hascalator
------------+ Prelude.scala   // the entry point
------------+ control         // the "home" for Monad and Applicative
------------+ data            // basic data types (ie Maybe, Either, List...)
------------+ dst             // immutable, purely functional data structures
------------+ math            // math types
------------+ typeclasses     // typeclasses
```

## Design ##

### Abstract data types ###

Abstract Data Types have different implementation is Scala than they have in Haskell.
Where Haskell defines a type (`Maybe`) and two constructor (`Just` and `Nothing`):

```haskell
data Maybe a = Just a | Nothing
```

on the other hand, in Scala with the **sealed trait pattern** we basically have three different types
(`Maybe`, `Just` and `None`).

```scala
sealed trait Maybe[+A]

private[data] final case class Just[A](value: A) extends Maybe[A]
private[data]       case object None             extends Maybe[Nothing]
```

To avoid to publish all the types, the only public type is
the trait one, the more specific types are kept private. New values are created
using smart constructors defined in the companion object for the trait.

```scala
object Maybe {
  def just[A](x: A): Maybe[A]
  def none[A]:       Maybe[A]
}
```

with explicit return types, the type for the constructed value will always be the trait.

### Strict vs Lazy ###

Haskell functions lose their semantics when translated in Scala (a strict language).
Even in this context, where functions don't loose all their functionality during the
translation they are kept in the API.

For instance, `(++) :: [a] -> [a] -> [a]` (the function that appends two lists) allows
infinite lists. The `data.List` implemented in Scala is not infinite and it's strictly
evaluated. Technically it's still possible to build an infinite list, but with the
strict evaluation the append operation will be rather useless (ie. the `append` operation
will never terminate).

Other functions, `iterate :: (a -> a) -> a -> [a]`, are not working in a strict
evaluated context and therefore were left out of the API.

### Total functions ###

Even if Haskell is much pure language than Scala, there are in the `Prelude`
example of non-total functions (ie `head :: [a] -> a`). The corresponding methods
have the same semantics throwing an `Exception`.

Where this is not a heavy burden, the API provides alternatives in the form of
`PartialFunction`s or different return types that encode the possibility for missing
return values (`Maybe` or `Either` are returned in this case).

### From functions to methods ###

On this topic there are two main differences between Haskell and Scala. Scala is
an hybrid language, mixing object oriented with functional programming. In former
world, usually we have an instance of some object and we invoke methods against
these instances instead of passing arguments to *stand alone* functions.

The classic example is the `head` function for Lists:

`head(myList)` vs `myList.head`

The object oriented style was favored, and almost all functions for the basic data
types (`List`, `Maybe`, `Either`...) were translated to methods.

Scala has a local type inference and tries to infer the types from left to right,
one argument list at the time. To help the compiler to infer the types, the order
of arguments to methods have been changed from the Haskell corresponding functions.

### Purity ###

It's possible to reproduce the non-strict semantic of Haskell in Scala, but it
takes a lot of machinery. On the other hand, reproduce the purity approach
followed by Haskell takes just discipline in the implementation.

This library is mostly strict in its evaluation, but the data structures are
immutable, persistent and mostly pure.
To improve performance impurity and mutability are used in the implementation, but
this is hidden from the API clients.

### Typeclasses ###

Typeclasses were widely used, also in replacement for the common *Java-esque*
practices (`hashcode`, `equals` and `toString` just to name few).
They are a first class element in the Haskell programming language, for Scala
the implementation is heavy on implicit values.

## References ##

* Adriaan Moors, Frank Piessens, and Martin Odersky. __Generics of a higher kind.__ In Proceedings of the 23rd ACM SIGPLAN conference on Object-oriented programming systems languages and applications (OOPSLA '08). ACM, New York, NY, USA, 423-438, 2008.
* Bruno CdS Oliveira, Adriaan Moors, and Martin Odersky. __Type classes as objects and implicits.__ ACM Sigplan Notices. Vol. 45. No. 10. ACM, 2010.
* Danielle Ashley. __Type lambdas and kind projector__. Blog post retried from [here](http://underscore.io/blog/posts/2016/12/05/type-lambdas.html), 2016.
* Gerth S. Brodal and Chris Okasaki. Optimal purely functional priority queues. J. Functional Programming, 6(6):839–857, 1996
* Haskell docs: `Prelude`. Retrived from [here](http://hackage.haskell.org/package/base-4.9.0.0/docs/Prelude.html)
* Larry Liu Xinyu. __Elementary Algorithms__. Retrived from [here](https://github.com/liuxinyu95/AlgoXY/releases/download/v0.618033/elementary-algorithms.pdf), 2016.
* Louis Wasserman. __Playing with Priority Queues.__ The Monad Reader, Issue 16 (pp. 37–52), 2010.
* Martin Odersky, Lex Spoon, and Bill Venners. 2016. __Programming in Scala, Third Edition__. Artima
* Martin Odersky and Matthias Zenger. __Scalable component abstractions.__ In R. Johnson and R. P. Gabriel, editors, OOPSLA, pages 41–57. ACM, 2005.
* Nick Stanchenko. __Unzipping Immutability__. Scala By the Bay. Retrieved from [here](https://www.youtube.com/watch?v=dOj-wk5MQ3k), 2016.
* Paul Chiusano, Rúnar Bjarnason. __Functional Programming in Scala__. Manning Publications, 2014.
* Ralf Hinze and Ross Paterson. __Finger trees: a simple general-purpose data structure__. J. Funct. Program. 16, 2 (March 2006), 197-217. 
* Stephen Adams. __Implementing Sets Efficiently in a Functional.__.
* Stephen Compall. __Higher-kinded types: the difference between giving up, and moving forward__. Blog post retried from [here](http://typelevel.org/blog/2016/08/21/hkts-moving-forward.html), 2016.
* Typelevel `Cats`: Lightweight, modular, and extensible library for functional programming. [https://github.com/typelevel/cats](https://github.com/typelevel/cats)
* Typelevel `Dogs`: data structures for pure functional programming in scala. [https://github.com/stew/dogs](https://github.com/stew/dogs)

## Contribution policy ##

Contributions via GitHub pull requests are gladly accepted from their original author. Along with any pull requests, please state that the contribution is your original work and that you license the work to the project under the project's open source license. Whether or not you state this explicitly, by submitting any copyrighted material via pull request, email, or other means you agree to license the material under the project's open source license and warrant that you have the legal authority to do so.

## License ##

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").