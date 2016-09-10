---
layout: default
title:  "Data.Either"
---

# `Data.Either`

The `Either` type represents values with two possibilities: a value of type `Either[A, B]` is either _Left(a)_ or _Right(b)_.

The `Either` type is sometimes used to represent a value which is either correct or an error; by convention, the _Left_ constructor is used to hold an error value and the _Right_ constructor is used to hold a correct value (mnemonic: "right" also means "correct").

## Examples

The type `Either[String, Int]` is the type of values which can be either a `String` or an `Int`. The _Left_ constructor can be used only on `String`s, and the _Right_ constructor can be used only on `Int`s:

```tut:silent
import io.hascalator._
import Prelude._
import Either._
```

```tut
val s: Either[String, Int] = left("foo")
val n: Either[String, Int] = right(3)
```

The `map` will ignore _Left_ values, but will apply the supplied function to values contained in a _Right_:

```tut
s.map(_ * 2)
n.map(_ * 2)
```

`either`: case analysis for the `Either` type. If the value is _Left(a)_, apply the first function to `a`; if it is _Right(b)_, apply the second function to `b`.

```tut
s.either(_.length)(_ * 2)
n.either(_.length)(_ * 2)
```

`lefts`: extracts from a list of `Either` all the _Left_ elements. All the _Left_ elements are extracted in order.

```tut
val xs = List(left("foo"), right(3), left("bar"), right(7), left("baz"))
lefts(xs)
```

`rights`: extracts from a list of `Either` all the _Right_ elements. All the _Right_ elements are extracted in order.

```tut
val ys = List(left("foo"), right(3), left("bar"), right(7), left("baz"))
rights(ys)
```

`isLeft`: return `true` if the given value is a Left-value, `false` otherwise.

```tut
left("foo").isLeft
right(1).isLeft
```

`isRight`: return `true` if the given value is a Right-value, `false` otherwise.

```tut
left("foo").isRight
right(1).isRight
```

`partitionEithers`: partitions a list of `Either` into two lists. All the _Left_ elements are extracted, in order, to the first component of the output. Similarly the _Right_ elements are extracted to the second component of the output.

```tut
val zs = List[Either[String, Int]](left("foo"), right(3), left("bar"), right(7), left("baz"))
partitionEithers(xs)
```

The pair returned by `partitionEithers(x)` should be the same pair as `(lefts(x), rights(x))`:

```tut
(lefts(zs), rights(zs))
```
