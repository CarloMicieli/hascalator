---
layout: default
title:  "Data.List"
---

# Data.List

Operations on lists. The `List` is an immutable, inductive data type defined either as

  * the empty list `Nil`;
  * the constructed list `Cons`, with an `head` and a `tail`.

```scala
import io.hascalator._
import Prelude._
```

```scala
scala> val xs = List(1, 23, 15, 42, 77)
xs: io.hascalator.data.List[Int] = [1, 23, 15, 42, 77]

scala> val ys = List(10, 34, 5, 55, 233)
ys: io.hascalator.data.List[Int] = [10, 34, 5, 55, 233]
```

`head` and `tail` are not total functions in this implementation therefore they will fail (throwing an exception) whether the current list is empty.

```scala
scala> List.empty[Int].head
io.hascalator.ApplicationException: *** Exception: List.head: empty list
  at io.hascalator.Prelude$.error(Prelude.scala:159)
  at io.hascalator.data.Nil$.head(List.scala:1137)
  at io.hascalator.data.Nil$.head(List.scala:1136)
  ... 246 elided
```

```scala
scala> List.empty[Int].tail
io.hascalator.ApplicationException: *** Exception: List.tail: empty list
  at io.hascalator.Prelude$.error(Prelude.scala:159)
  at io.hascalator.data.Nil$.tail(List.scala:1138)
  at io.hascalator.data.Nil$.tail(List.scala:1136)
  ... 262 elided
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

```scala
scala> xs ++ ys
res2: io.hascalator.data.List[Int] = [1, 23, 15, 42, 77, 10, 34, 5, 55, 233]
```

`head`: extract the first element of a list, which must be non-empty.

```scala
scala> List(1, 2, 3).head
res3: Int = 1
```

`last`: extract the elements after the head of a list, which must be non-empty.

```scala
scala> List(1, 2, 3).last
res4: Int = 3
```

```scala
scala> List().last
<console>:19: warning: dead code following this construct
       List().last
              ^
error: No warnings can be incurred under -Xfatal-warnings.
```

`tail`: extract the elements after the head of a list, which must be non-empty.

```scala
scala> xs.tail
res6: io.hascalator.data.List[Int] = [23, 15, 42, 77]
```

`init`: return all the elements of a list except the last one. The list must be non-empty.

```scala
scala> List(1, 2, 3).init
res7: io.hascalator.data.List[Int] = [1, 2]
```

`unCons`: decompose a list into its `head` and `tail`. If the list is empty, returns `none`.
If the list is non-empty, returns `just(x, xs)`, where `x` is the `head` of the list and `xs` its `tail`.

```scala
scala> xs.unCons
res8: io.hascalator.data.Maybe[(Int, io.hascalator.data.List[Int])] = Just((1,[23, 15, 42, 77]))
```

`isEmpty` test whether the list is empty.

```scala
scala> xs.isEmpty
res9: io.hascalator.Prelude.Boolean = false

scala> List.empty[Int].isEmpty
res10: io.hascalator.Prelude.Boolean = true
```
`length` Returns the size/length of a finite structure as an `Int`.

```scala
scala> xs.length
res11: io.hascalator.Prelude.Int = 5

scala> List.empty[Int].length
res12: io.hascalator.Prelude.Int = 0
```

## List transformations

`xs.map(f)` is the list obtained by applying `f` to each element of `xs`, i.e.,

```scala
scala> List(1, 2, 3, 4, 5, 6).map(_ * 2)
res13: io.hascalator.data.List[Int] = [2, 4, 6, 8, 10, 12]
```

`reverse` returns the elements of `xs` in reverse order.

```scala
scala> List(1, 2, 3, 4, 5, 6).reverse
res14: io.hascalator.data.List[Int] = [6, 5, 4, 3, 2, 1]
```

`intersperse`: the `intersperse` function takes an element and a list and _"intersperses"_ that element between the elements of the list. For example,

```scala
scala> List('h', 'e', 'l', 'l', 'o').intersperse('-')
res15: io.hascalator.data.List[Char] = [h, -, e, -, l, -, l, -, o]
```

`intercalate`: `xss.intercalate(xs)` is equivalent to `(xss intersperse xs).concat`. It inserts the list `xs` in between the lists in `xss` and concatenates the result.

```scala
scala> List(List(1, 2), List(4), List(5, 6)).intercalate(List(0))
res16: io.hascalator.data.List[Int] = [1, 2, 0, 4, 0, 5, 6]
```

`permutations`: the `permutations` function returns the list of all permutations of the argument.

```scala
scala> List('a', 'b', 'c').permutations
res17: io.hascalator.data.List[io.hascalator.data.List[Char]] = [[a, b, c], [a, c, b], [b, a, c], [b, c, a], [c, a, b], [c, b, a]]
```

## Reducing lists (folds)

`foldLeft`: Left-associative fold of a structure. In the case of lists, `foldLeft`, when applied to a binary operator, a starting value (typically the left-identity
of the operator), and a list, reduces the list using the binary operator, from left to right:

```
List(x1, x2, ..., xn).foldLeft(z)(f)  == (...((z `f` x1) `f` x2) `f`...) `f` xn
```
                                                  
Note that to produce the outermost application of the operator the entire input list must be traversed.

```scala
scala> List(1, 2, 3, 4, 5).foldLeft(10)(_ + _)
res18: Int = 25
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
             
```scala
scala> List(1, 2, 3, 4, 5).foldRight(10)(_ + _)
res19: Int = 25
```

`foldRight1`: a variant of `foldRight` that has no base case, and thus may only be applied to non-empty structures.

```scala
scala> List(1, 2, 3, 4, 5).foldRight1(_ + _)
res20: Int = 15
```

## Special folds

`concat`: the concatenation of all the elements of a container of lists.

```scala
scala> List(List(1, 2), List(3), List(4, 5)).concat
res21: io.hascalator.data.List[Int] = [1, 2, 3, 4, 5]
```

`concatMap`: Map a function over all the elements of a container and concatenate the resulting lists.
             
```scala
scala> List(1, 2, 3, 4, 5).concatMap(x => List(x, x * 2))
res22: io.hascalator.data.List[Int] = [1, 2, 2, 4, 3, 6, 4, 8, 5, 10]
```

## Building lists

### Scans

`scanLeft` is similar to `foldLeft`, but returns a list of successive reduced values from the left:

```
List(x1, x2, ...).scanLeft(z)(f) == [z, z `f` x1, (z `f` x1) `f` x2, ...]
```

```scala
scala> List(1, 2, 3, 4, 5).scanLeft(0)(_ + _)
res23: io.hascalator.data.List[Int] = [0, 1, 3, 6, 10, 15]
```

`scanRight`: is the right-to-left dual of `scanLeft`. Note that:

```
xs.scanRight(z)(f).head == xs.foldRight(z)(f)
```

```scala
scala> List(1, 2, 3, 4, 5).scanRight(0)(_ + _)
res24: io.hascalator.data.List[Int] = [15, 14, 12, 9, 5, 0]
```

## Sublists

### Extracting sublists

`take(n)`, applied to a list `xs`, returns the prefix of `xs` of length `n`, or `xs` itself if `n > xs.length`

```scala
scala> List(1, 2, 3, 4, 5) take 3
res25: io.hascalator.data.List[Int] = [1, 2, 3]

scala> List(1, 2) take 3
res26: io.hascalator.data.List[Int] = [1, 2]

scala> List.empty[Int] take 3
res27: io.hascalator.data.List[io.hascalator.Prelude.Int] = []

scala> List(1, 2).take(-1)
res28: io.hascalator.data.List[Int] = []

scala> List(1, 2) take 0
res29: io.hascalator.data.List[Int] = []
```

`xs.drop(n)` returns the suffix of `xs` after the first `n` elements, or the empty list if `n > xs.length`

```scala
scala> List(1, 2, 3, 4, 5) drop 3
res30: io.hascalator.data.List[Int] = [4, 5]

scala> List(1, 2) drop 3
res31: io.hascalator.data.List[Int] = []

scala> List.empty[Int] drop 3
res32: io.hascalator.data.List[io.hascalator.Prelude.Int] = []

scala> List(1, 2).drop(-1)
res33: io.hascalator.data.List[Int] = []

scala> List(1, 2) drop 0
res34: io.hascalator.data.List[Int] = [1, 2]
```

`xs.splitAt(n)` returns a tuple where first element is `xs` prefix of length `n` and second element is the remainder of the list

```scala
scala> List(1, 2, 3, 4, 5, 6) splitAt 3
res35: (io.hascalator.data.List[Int], io.hascalator.data.List[Int]) = ([1, 2, 3],[4, 5, 6])

scala> List(1, 2, 3) splitAt 0
res36: (io.hascalator.data.List[Int], io.hascalator.data.List[Int]) = ([],[1, 2, 3])
```

`takeWhile`, applied to a predicate `p` and a list `xs`, returns the longest prefix (possibly empty) of `xs` of elements that satisfy `p`:

```scala
scala> List(1, 2, 3, 4, 5, 6).takeWhile(_ % 2 == 0)
res37: io.hascalator.data.List[Int] = []
```

`xs.dropWhile(p)` returns the suffix remaining after `xs.takeWhile(p)`:

```scala
scala> List(1, 2, 3, 4, 5, 6).dropWhile(_ % 2 == 0)
res38: io.hascalator.data.List[Int] = [1, 2, 3, 4, 5, 6]
```

`dropWhileEnd`: the `dropWhileEnd` function drops the largest suffix of a list in which the given predicate holds for all elements. For example:

```scala
scala> List(1, 2, 3, 4, 5, 6).dropWhileEnd(_ < 5)
res39: io.hascalator.data.List[Int] = [1, 2, 3, 4, 5, 6]

scala> List(1, 2, 3, 4, 5, 6).dropWhileEnd(_ > 5)
res40: io.hascalator.data.List[Int] = [1, 2, 3, 4, 5]
```

`span`, applied to a predicate `p` and a list `xs`, returns a tuple where first element is longest prefix (possibly empty)
of `xs` of elements that satisfy `p` and second element is the remainder of the list:

```scala
scala> List(1, 2, 3, 4, 5, 6).span(_ < 3)
res41: (io.hascalator.data.List[Int], io.hascalator.data.List[Int]) = ([1, 2],[3, 4, 5, 6])
```

`break`, applied to a predicate `p` and a list `xs`, returns a tuple where first element is longest prefix (possibly empty)
of `xs` of elements that do not satisfy p and second element is the remainder of the list:

```scala
scala> List(1, 2, 3) break (_ < 9)
res42: (io.hascalator.data.List[Int], io.hascalator.data.List[Int]) = ([],[1, 2, 3])

scala> List(1, 2, 3) break (_ > 9)
res43: (io.hascalator.data.List[Int], io.hascalator.data.List[Int]) = ([1, 2, 3],[])
```

`stripPrefix`: The `stripPrefix` function drops the given prefix from a list. It returns ''None'' if the list did not start
with the prefix given, or Just the list after the prefix, if it does.

```scala
scala> List(1, 2, 3, 4, 5, 6).stripPrefix((List(1, 2, 3)))
res44: io.hascalator.data.Maybe[io.hascalator.data.List[Int]] = Just([4, 5, 6])

scala> List(1, 2, 3).stripPrefix((List(1, 2, 3)))
res45: io.hascalator.data.Maybe[io.hascalator.data.List[Int]] = Just([])

scala> List(6, 5, 4, 1, 2, 3).stripPrefix((List(1, 2, 3)))
res46: io.hascalator.data.Maybe[io.hascalator.data.List[Int]] = None
```

`group` The group function takes a list and returns a list of lists such that the concatenation of the result is equal
to the argument. Moreover, each sublist in the result contains only equal elements. For example,

```scala
scala> List(1, 1, 2, 3, 3, 3, 4, 4, 5).group
res47: io.hascalator.data.List[io.hascalator.data.List[Int]] = [[1, 1], [2], [3, 3, 3], [4, 4], [5]]
```

`inits` The `inits` function returns all initial segments of the argument, shortest first. For example,

```scala
scala> List(1, 2, 3).inits
res48: io.hascalator.data.List[io.hascalator.data.List[Int]] = [[], [1], [1, 2], [1, 2, 3]]
```

`tails` The `tails` function returns all final segments of the argument, longest first. For example,

```scala
scala> List(1, 2, 3).tails
res49: io.hascalator.data.List[io.hascalator.data.List[Int]] = [[1, 2, 3], [2, 3], [3], []]
```

The `partition` function takes a predicate a list and returns the pair of lists of elements which do and do not
satisfy the predicate, respectively; i.e.,

```scala
scala> List(1, 2, 3, 4, 5, 6).partition(_ < 3)
res50: (io.hascalator.data.List[Int], io.hascalator.data.List[Int]) = ([1, 2],[3, 4, 5, 6])
```

`filter`, applied to a predicate and a list, returns the list of those elements that satisfy the predicate; i.e.,

```scala
scala> List(1, 2, 3, 4, 5, 6).filter(_ < 3)
res51: io.hascalator.data.List[Int] = [1, 2]
```

## Searching lists

### Searching by equality

### Searching with a predicate

`find`: The find function takes a predicate and a structure and returns the leftmost element of the structure matching the predicate, or Nothing if there is no such element.

```scala
scala> List(1, 2, 3, 4, 5) find (_ == 5)
res52: io.hascalator.data.Maybe[Int] = Just(5)

scala> List(1, 2, 3, 4, 5) find (_ != 5)
res53: io.hascalator.data.Maybe[Int] = Just(1)

scala> List(1, 2, 3, 4, 5) find (_ > 9)
res54: io.hascalator.data.Maybe[Int] = None
```

`filter` applied to a predicate and a list, returns the list of those elements that satisfy the predicate; i.e.,

```scala
scala> List(1, 2, 3, 4, 5) filter (_ != 0)
res55: io.hascalator.data.List[Int] = [1, 2, 3, 4, 5]

scala> List(1, 2, 3, 4, 5) filter (_ == 0)
res56: io.hascalator.data.List[Int] = []
```

`partition` The partition function takes a predicate a list and returns the pair of lists of elements which do and do not satisfy the predicate, respectively; i.e.,

```scala
scala> List(1, 2, 3, 4, 5) partition (x => x / 2 != 0)
res57: (io.hascalator.data.List[Int], io.hascalator.data.List[Int]) = ([2, 3, 4, 5],[1])

scala> List(1, 2, 3, 4, 5) partition (x => x / 2 == 0)
res58: (io.hascalator.data.List[Int], io.hascalator.data.List[Int]) = ([1],[2, 3, 4, 5])
```

## Zipping and unzipping lists

`zip` takes two lists and returns a list of corresponding pairs. If one input list is short, excess elements of the
longer list are discarded.

```scala
scala> List(1, 2, 3, 4) zip List('a', 'b', 'c', 'd')
res59: io.hascalator.data.List[(Int, Char)] = [(1,a), (2,b), (3,c), (4,d)]
```
