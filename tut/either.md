---
layout: default
title:  "Data.Either"
---

# Data.Either

The `Either` type represents values with two possibilities: a value of type `Either[A, B]` is either _Left(a)_ or _Right(b)_.

The `Either` type is sometimes used to represent a value which is either correct or an error; by convention, the _Left_ constructor is used to hold an error value and the _Right_ constructor is used to hold a correct value (mnemonic: "right" also means "correct").

## Examples

The type `Either[String, Int]` is the type of values which can be either a `String` or an `Int`. The _Left_ constructor can be used only on `String`s, and the _Right_ constructor can be used only on `Int`s:

```scala
import io.hascalator._
import Prelude._
import Either._
```

```scala
scala> val s: Either[String, Int] = left("foo")
s: io.hascalator.Prelude.Either[io.hascalator.Prelude.String,io.hascalator.Prelude.Int] = Left(foo)

scala> val n: Either[String, Int] = right(3)
n: io.hascalator.Prelude.Either[io.hascalator.Prelude.String,io.hascalator.Prelude.Int] = Right(3)
```

The `map` will ignore _Left_ values, but will apply the supplied function to values contained in a _Right_:

```scala
scala> s.map(_ * 2)
res0: io.hascalator.data.Either[io.hascalator.Prelude.String,Int] = Left(foo)

scala> n.map(_ * 2)
res1: io.hascalator.data.Either[io.hascalator.Prelude.String,Int] = Right(6)
```

`either`: case analysis for the `Either` type. If the value is _Left(a)_, apply the first function to `a`; if it is _Right(b)_, apply the second function to `b`.

```scala
scala> s.either(_.length)(_ * 2)
res2: Int = 3

scala> n.either(_.length)(_ * 2)
res3: Int = 6
```

`lefts`: extracts from a list of `Either` all the _Left_ elements. All the _Left_ elements are extracted in order.

```scala
scala> val xs = List(left("foo"), right(3), left("bar"), right(7), left("baz"))
xs: io.hascalator.data.List[io.hascalator.data.Either[String,Int]] = [Left(foo), Right(3), Left(bar), Right(7), Left(baz)]

scala> lefts(xs)
res4: io.hascalator.data.List[String] = [foo, bar, baz]
```

`rights`: extracts from a list of `Either` all the _Right_ elements. All the _Right_ elements are extracted in order.

```scala
scala> val ys = List(left("foo"), right(3), left("bar"), right(7), left("baz"))
ys: io.hascalator.data.List[io.hascalator.data.Either[String,Int]] = [Left(foo), Right(3), Left(bar), Right(7), Left(baz)]

scala> rights(ys)
res5: io.hascalator.data.List[Int] = [3, 7]
```

`isLeft`: return `true` if the given value is a Left-value, `false` otherwise.

```scala
scala> left("foo").isLeft
res6: io.hascalator.Prelude.Boolean = true

scala> right(1).isLeft
res7: io.hascalator.Prelude.Boolean = false
```

`isRight`: return `true` if the given value is a Right-value, `false` otherwise.

```scala
scala> left("foo").isRight
res8: io.hascalator.Prelude.Boolean = false

scala> right(1).isRight
res9: io.hascalator.Prelude.Boolean = true
```

`partitionEithers`: partitions a list of `Either` into two lists. All the _Left_ elements are extracted, in order, to the first component of the output. Similarly the _Right_ elements are extracted to the second component of the output.

```scala
scala> val zs = List[Either[String, Int]](left("foo"), right(3), left("bar"), right(7), left("baz"))
zs: io.hascalator.data.List[io.hascalator.Prelude.Either[io.hascalator.Prelude.String,io.hascalator.Prelude.Int]] = [Left(foo), Right(3), Left(bar), Right(7), Left(baz)]

scala> partitionEithers(xs)
res10: (io.hascalator.data.List[String], io.hascalator.data.List[Int]) = ([foo, bar, baz],[3, 7])
```

The pair returned by `partitionEithers(x)` should be the same pair as `(lefts(x), rights(x))`:

```scala
scala> (lefts(zs), rights(zs))
res11: (io.hascalator.data.List[io.hascalator.Prelude.String], io.hascalator.data.List[io.hascalator.Prelude.Int]) = ([foo, bar, baz],[3, 7])
```
