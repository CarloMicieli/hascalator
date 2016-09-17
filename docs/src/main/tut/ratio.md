---
layout: default
title:  "Data.Ratio"
---

# Data.Ratio

```tut:silent
import io.hascalator._
import Prelude._
import data.Ratio
```

Rational numbers, with numerator and denominator of some `Integral` type.

```tut
val oneThird = Ratio[Int](1, 3)
val twoThirds = Ratio[Int](2, 3)
```

Trying to create a new `Ratio` with _denominator_ equals to 0 will result
in an error:

```tut:fail
Ratio[Int](4, 0)
```

Since _denominator_ may be equal to one, every integer is a `Ratio` number:

```tut
Ratio[Int](42, 1)
Ratio[Int](42)
```

Both `numerator` and `denominator` values are reduced when a new `Ratio`
number is created:

```tut
val threeFourths = Ratio[Int](9, 16)
```

`numerator` extract the numerator of the ratio in reduced form: the numerator and denominator have no common factor and the denominator is positive.

```tut
oneThird.numerator
twoThirds.numerator
```

`denominator` extract the denominator of the ratio in reduced form: the numerator and denominator have no common factor and the denominator is positive.

```tut
oneThird.denominator
twoThirds.denominator
```

## Operations

*Addition*

```tut
oneThird + twoThirds
```

*Subtraction*

```tut
oneThird - twoThirds
```

*Multiplication*

```tut
oneThird * twoThirds
```

*Division*

```tut
oneThird / twoThirds
```

## Typeclasses

`Ratio` was made an instance of `Eq`, `Ord`, `Enum` and `Show` typeclasses

```tut
Show[Ratio[Int]].show(twoThirds)
```

```tut
Eq[Ratio[Int]].eq(twoThirds, twoThirds)
Eq[Ratio[Int]].eq(oneThird, twoThirds)
```

```tut
Ord[Ratio[Int]].compare(twoThirds, twoThirds)
Ord[Ratio[Int]].compare(twoThirds, oneThird)
Ord[Ratio[Int]].compare(oneThird, twoThirds)
```

