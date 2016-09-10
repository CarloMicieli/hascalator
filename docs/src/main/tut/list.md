---
layout: default
title:  "Data.List"
---

# `Data.List`

Operations on lists. The `List` is an immutable, inductive data type defined either as

  * the empty list `Nil`;
  * the constructed list `Cons`, with an `head` and a `tail`.

```tut:silent
import io.hascalator._
import Prelude._
```

```tut
val xs = List(1, 23, 15, 42, 77)
val ys = List(10, 34, 5, 55, 233)
```

`head` and `tail` are not total functions in this implementation therefore they will fail (throwing an exception) whether the current list is empty.

```tut:fail
List.empty[Int].head
```

```tut:fail
List.empty[Int].tail
```

## Abstract definition

The abstract list type `L` with elements of some type `E` (a _monomorphic list_) is defined by the following functions:

```
nil: () → L
cons: E × L → L
first: L → E
rest: L → L
```

with the axioms

```
first (cons (e, l)) = e
rest (cons (e, l)) = l
```

for any element `e` and any list `l`. It is implicit that

```
cons (e, l) ≠ l
cons (e, l) ≠ e
cons (e1, l1) = cons (e2, l2) if e1 = e2 and l1 = l2
```

Note that `first (nil ())` and `rest (nil ())` are not defined.

## Performance

```
FlatMapBenchmarks.flatMapArrayBenchmark      thrpt   10       4644.779 ±      34.243  ops/s
FlatMapBenchmarks.flatMapListBenchmark       thrpt   10        780.974 ±       7.876  ops/s
FlatMapBenchmarks.flatMapScalaListBenchmark  thrpt   10        840.812 ±      28.638  ops/s
FoldBenchmarks.foldLeftArrayBenchmark        thrpt   10      23632.711 ±     145.108  ops/s
FoldBenchmarks.foldLeftListBenchmark         thrpt   10      19286.125 ±     302.248  ops/s
FoldBenchmarks.foldLeftScalaListBenchmark    thrpt   10      38322.952 ±     429.004  ops/s
FoldBenchmarks.foldRightArrayBenchmark       thrpt   10      22591.765 ±     263.938  ops/s
FoldBenchmarks.foldRightListBenchmark        thrpt   10       9076.899 ±     101.025  ops/s
FoldBenchmarks.foldRightScalaListBenchmark   thrpt   10      12181.539 ±     220.144  ops/s
LengthBenchmarks.lengthArrayBenchmark        thrpt   10  529300473.076 ± 4227815.389  ops/s
LengthBenchmarks.lengthListBenchmark         thrpt   10      16629.287 ±     211.113  ops/s
LengthBenchmarks.lengthScalaListBenchmark    thrpt   10      51903.725 ±     221.623  ops/s
MapBenchmarks.mapArrayBenchmark              thrpt   10      14366.091 ±     324.271  ops/s
MapBenchmarks.mapListBenchmark               thrpt   10      18217.327 ±     186.161  ops/s
MapBenchmarks.mapScalaListBenchmark          thrpt   10      15619.095 ±     165.844  ops/s
```

Machine used for the benchmarks:

* OS: Ubuntu 16.04 xenial
* Kernel: x86_64 Linux 4.4.0-36-generic
* CPU: Intel Core i7-6700K CPU @ 4.2GHz
* GPU: GeForce GTX 760
* RAM: 32127MiB
* Java: Java(TM) SE Runtime Environment (build 1.8.0_91-b14)
* Scala: 2.11.8

## Basic functions

`++` append two lists

```tut
xs ++ ys
```

`head`: extract the first element of a list, which must be non-empty.

```tut
List(1, 2, 3).head
```

`last`: extract the elements after the head of a list, which must be non-empty.

```tut
List(1, 2, 3).last
```

```tut:fail
List().last
```

`tail`: extract the elements after the head of a list, which must be non-empty.

```tut
xs.tail
```

`init`: return all the elements of a list except the last one. The list must be non-empty.

```tut
List(1, 2, 3).init
```

`unCons`: decompose a list into its `head` and `tail`. If the list is empty, returns `none`.
If the list is non-empty, returns `just(x, xs)`, where `x` is the `head` of the list and `xs` its `tail`.

```tut
xs.unCons
```

`isEmpty` test whether the list is empty.

```tut
xs.isEmpty
List.empty[Int].isEmpty
```
`length` Returns the size/length of a finite structure as an `Int`.

```tut
xs.length
List.empty[Int].length
```

## List transformations

`xs.map(f)` is the list obtained by applying `f` to each element of `xs`, i.e.,

```tut
List(1, 2, 3, 4, 5, 6).map(_ * 2)
```

`reverse` returns the elements of `xs` in reverse order.

```tut
List(1, 2, 3, 4, 5, 6).reverse
```

`intersperse`: the `intersperse` function takes an element and a list and _"intersperses"_ that element between the elements of the list. For example,

```tut
List('h', 'e', 'l', 'l', 'o').intersperse('-')
```

`intercalate`: `xss.intercalate(xs)` is equivalent to `(xss intersperse xs).concat`. It inserts the list `xs` in between the lists in `xss` and concatenates the result.

```tut
List(List(1, 2), List(4), List(5, 6)).intercalate(List(0))
```

## Reducing lists (folds)

`foldLeft`: Left-associative fold of a structure. In the case of lists, `foldLeft`, when applied to a binary operator, a starting value (typically the left-identity
of the operator), and a list, reduces the list using the binary operator, from left to right:

```
List(x1, x2, ..., xn).foldLeft(z)(f)  == (...((z `f` x1) `f` x2) `f`...) `f` xn
```
                                                  
Note that to produce the outermost application of the operator the entire input list must be traversed.

```tut
List(1, 2, 3, 4, 5).foldLeft(10)(_ + _)
```

`foldLeft1`: a variant of `foldLeft` that has no base case, and thus may only be applied to non-empty structures.

```
List(1, 2, 3, 4, 5).foldLeft1(_ + _)
```

`foldRight`: Right-associative fold of a structure. In the case of lists, `foldRight`, when applied to a binary operator, a starting value (typically the right-identity
of the operator), and a list, reduces the list using the binary operator, from right to left:

```
List(x1, x2, ..., xn).foldRight(z)(f)  == x1 `f` (x2 `f` ... (xn `f` z)...)
```
             
```tut
List(1, 2, 3, 4, 5).foldRight(10)(_ + _)
```

`foldRight1`: a variant of `foldRight` that has no base case, and thus may only be applied to non-empty structures.

```tut
List(1, 2, 3, 4, 5).foldRight1(_ + _)
```

## Special folds

`concat`: the concatenation of all the elements of a container of lists.

```tut
List(List(1, 2), List(3), List(4, 5)).concat
```

`concatMap`: Map a function over all the elements of a container and concatenate the resulting lists.
             
```tut
List(1, 2, 3, 4, 5).concatMap(x => List(x, x * 2))
```

## Building lists

### Scans

`scanLeft` is similar to `foldLeft`, but returns a list of successive reduced values from the left:

```
List(x1, x2, ...).scanLeft(z)(f) == [z, z `f` x1, (z `f` x1) `f` x2, ...]
```

```tut
List(1, 2, 3, 4, 5).scanLeft(0)(_ + _)
```

`scanRight`: is the right-to-left dual of `scanLeft`. Note that:

```
xs.scanRight(z)(f).head == xs.foldRight(z)(f)
```

```tut
List(1, 2, 3, 4, 5).scanRight(0)(_ + _)
```

## Sublists

`xs.take(n)`, applied to a list `xs`, returns the prefix of `xs` of length `n`, or `xs` itself if `n > xs.length`

```tut
xs.take(0)
xs.take(2)
xs.take(10)
```

`xs.drop(n)` returns the suffix of `xs` after the first `n` elements, or the empty list if `n > xs.length`

```tut
xs.drop(0)
xs.drop(2)
xs.drop(10)
```

`xs.splitAt(n)` returns a tuple where first element is `xs` prefix of length `n` and second element is the remainder of the list

```tut
List(1, 2, 3, 4, 5, 6).splitAt(3)
```

`takeWhile`, applied to a predicate `p` and a list `xs`, returns the longest prefix (possibly empty) of `xs` of elements that satisfy `p`:

```tut
List(1, 2, 3, 4, 5, 6).takeWhile(_ % 2 == 0)
```

`xs.dropWhile(p)` returns the suffix remaining after `xs.takeWhile(p)`:

```tut
List(1, 2, 3, 4, 5, 6).dropWhile(_ % 2 == 0)
```

`span`, applied to a predicate `p` and a list `xs`, returns a tuple where first element is longest prefix (possibly empty)
of `xs` of elements that satisfy `p` and second element is the remainder of the list:

```tut
List(1, 2, 3, 4, 5, 6).span(_ < 3)
```

The `partition` function takes a predicate a list and returns the pair of lists of elements which do and do not
satisfy the predicate, respectively; i.e.,

```tut
List(1, 2, 3, 4, 5, 6).partition(_ < 3)
```

`filter`, applied to a predicate and a list, returns the list of those elements that satisfy the predicate; i.e.,

```tut
List(1, 2, 3, 4, 5, 6).filter(_ < 3)
```

## Zipping and unzipping lists

`zip` takes two lists and returns a list of corresponding pairs. If one input list is short, excess elements of the
longer list are discarded.

```tut
List(1, 2, 3, 4) zip List('a', 'b', 'c', 'd')
```
