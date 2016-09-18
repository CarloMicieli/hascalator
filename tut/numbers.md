---
layout: default
title:  "Numbers"
---

# Numbers

## Integral types (integer-like types)

`Integral` types contain only whole numbers and not fractions. The most commonly used integral types are:

* `Integer`, which are arbitrary-precision integers, often called _"bignum"_ or _"big-integers"_ in other languages, and
* `Int`, which fixed-width machine-specific integers with a minimum guaranteed range of `−2^32` to `2^32 − 1`.

The workhorse for converting from integral types is `fromIntegral`, which
will convert from any `Integral` type into any `Num`eric type (which includes
`Int`, `Integer`, `Rational`, and `Double`):

```scala
def fromIntegral[A: Integral, B: Num](a: A): B
```

For example, given an `Int` value `n`, one does not simply take its square
root by typing `sqrt(n)`, since `sqrt` can only be applied to Floating-point
numbers. Instead, one must write `sqrt(fromIntegral(n))` to explicitly convert
`n` to a floating-point number. There are special cases for converting from `Integer`s:

```scala
def fromInteger[A: Integral](n: Int): A
```

as well as for converting to `Integer`s:

```scala
def toInteger[A: Integral](n: Int): A
```

