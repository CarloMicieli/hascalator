---
layout: default
title:  "Data.Ratio"
---

# Data.Ratio

```scala
import io.hascalator._
import Prelude._
import data.Ratio
```

Rational numbers, with numerator and denominator of some `Integral` type.

```scala
scala> val oneThird = Ratio[Int](1, 3)
oneThird: io.hascalator.data.Ratio[io.hascalator.Prelude.Int] = 1/3

scala> val twoThirds = Ratio[Int](2, 3)
twoThirds: io.hascalator.data.Ratio[io.hascalator.Prelude.Int] = 2/3
```

Trying to create a new `Ratio` with _denominator_ equals to 0 will result
in an error:

```scala
scala> Ratio[Int](4, 0)
java.lang.IllegalArgumentException: requirement failed
  at scala.Predef$.require(Predef.scala:212)
  at io.hascalator.Prelude$.require(Prelude.scala:193)
  at io.hascalator.data.Ratio$.apply(Ratio.scala:143)
  ... 250 elided
```

Since _denominator_ may be equal to one, every integer is a `Ratio` number:

```scala
scala> Ratio[Int](42, 1)
res1: io.hascalator.data.Ratio[io.hascalator.Prelude.Int] = 42

scala> Ratio[Int](42)
res2: io.hascalator.data.Ratio[io.hascalator.Prelude.Int] = 42
```

Both `numerator` and `denominator` values are reduced when a new `Ratio`
number is created:

```scala
scala> val threeFourths = Ratio[Int](9, 16)
threeFourths: io.hascalator.data.Ratio[io.hascalator.Prelude.Int] = 9/16
```

`numerator` extract the numerator of the ratio in reduced form: the numerator and denominator have no common factor and the denominator is positive.

```scala
scala> oneThird.numerator
res3: io.hascalator.Prelude.Int = 1

scala> twoThirds.numerator
res4: io.hascalator.Prelude.Int = 2
```

`denominator` extract the denominator of the ratio in reduced form: the numerator and denominator have no common factor and the denominator is positive.

```scala
scala> oneThird.denominator
res5: io.hascalator.Prelude.Int = 3

scala> twoThirds.denominator
res6: io.hascalator.Prelude.Int = 3
```

## Operations

*Addition*

```scala
scala> oneThird + twoThirds
res7: io.hascalator.data.Ratio[io.hascalator.Prelude.Int] = 1
```

*Subtraction*

```scala
scala> oneThird - twoThirds
res8: io.hascalator.data.Ratio[io.hascalator.Prelude.Int] = -1/3
```

*Multiplication*

```scala
scala> oneThird * twoThirds
res9: io.hascalator.data.Ratio[io.hascalator.Prelude.Int] = 2/9
```

*Division*

```scala
scala> oneThird / twoThirds
res10: io.hascalator.data.Ratio[io.hascalator.Prelude.Int] = 1/2
```

## Typeclasses

`Ratio` was made an instance of `Eq`, `Ord`, `Enum` and `Show` typeclasses

```scala
scala> Show[Ratio[Int]].show(twoThirds)
res11: io.hascalator.Prelude.String = 2/3
```

```scala
scala> Eq[Ratio[Int]].eq(twoThirds, twoThirds)
res12: io.hascalator.Prelude.Boolean = true

scala> Eq[Ratio[Int]].eq(oneThird, twoThirds)
res13: io.hascalator.Prelude.Boolean = false
```

```scala
scala> Ord[Ratio[Int]].compare(twoThirds, twoThirds)
res14: io.hascalator.typeclasses.Ordering = EQ

scala> Ord[Ratio[Int]].compare(twoThirds, oneThird)
res15: io.hascalator.typeclasses.Ordering = GT

scala> Ord[Ratio[Int]].compare(oneThird, twoThirds)
res16: io.hascalator.typeclasses.Ordering = LT
```

