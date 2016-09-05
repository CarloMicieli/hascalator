## `Data.List`

The `List` is an immutable, inductive data type defined either as 
  
  * the empty list `Nil`
  * the constructed list `Cons`, with an `head` and a `tail`
  
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
  at io.hascalator.data.Nil$.head(List.scala:997)
  at io.hascalator.data.Nil$.head(List.scala:996)
  ... 210 elided
```

```scala
scala> List.empty[Int].tail
io.hascalator.ApplicationException: *** Exception: List.tail: empty list
  at io.hascalator.Prelude$.error(Prelude.scala:159)
  at io.hascalator.data.Nil$.tail(List.scala:998)
  at io.hascalator.data.Nil$.tail(List.scala:996)
  ... 226 elided
```
### Basic functions

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

### List transformations

`xs.map(f) is the list obtained by applying `f` to each element of `xs`, i.e.,

```scala
scala> xs.map(_ * 2)
res13: io.hascalator.data.List[Int] = [2, 46, 30, 84, 154]
```

`reverse` returns the elements of `xs` in reverse order. 

```scala
scala> xs.reverse
res14: io.hascalator.data.List[Int] = [77, 42, 15, 23, 1]
```

### Sublists

`xs.take(n)`, applied to a list `xs`, returns the prefix of `xs` of length `n`, or `xs` itself if `n > xs.length`

```scala
scala> xs.take(0)
res15: io.hascalator.data.List[Int] = []

scala> xs.take(2)
res16: io.hascalator.data.List[Int] = [1, 23]

scala> xs.take(10)
res17: io.hascalator.data.List[Int] = [1, 23, 15, 42, 77]
```

`xs.drop(n)` returns the suffix of `xs` after the first `n` elements, or the empty list if `n > xs.length`

```scala
scala> xs.drop(0)
res18: io.hascalator.data.List[Int] = [1, 23, 15, 42, 77]

scala> xs.drop(2)
res19: io.hascalator.data.List[Int] = [15, 42, 77]

scala> xs.drop(10)
res20: io.hascalator.data.List[Int] = []
```

`xs.splitAt(n)` returns a tuple where first element is `xs` prefix of length `n` and second element is the remainder of the list

```scala
scala> List(1, 2, 3, 4, 5, 6).splitAt(3)
res21: (io.hascalator.data.List[Int], io.hascalator.data.List[Int]) = ([1, 2, 3],[4, 5, 6])
```

`takeWhile`, applied to a predicate `p` and a list `xs`, returns the longest prefix (possibly empty) of `xs` of elements that satisfy `p`:

```scala
scala> List(1, 2, 3, 4, 5, 6).takeWhile(_ % 2 == 0)
res22: io.hascalator.data.List[Int] = []
```

`xs.dropWhile(p)` returns the suffix remaining after `xs.takeWhile(p)`:

```scala
scala> List(1, 2, 3, 4, 5, 6).dropWhile(_ % 2 == 0)
res23: io.hascalator.data.List[Int] = [1, 2, 3, 4, 5, 6]
```

`span`, applied to a predicate `p` and a list `xs`, returns a tuple where first element is longest prefix (possibly empty) 
of `xs` of elements that satisfy `p` and second element is the remainder of the list:

```scala
scala> List(1, 2, 3, 4, 5, 6).span(_ < 3)
res24: (io.hascalator.data.List[Int], io.hascalator.data.List[Int]) = ([1, 2],[3, 4, 5, 6])
```

The `partition` function takes a predicate a list and returns the pair of lists of elements which do and do not 
satisfy the predicate, respectively; i.e.,

```scala
scala> List(1, 2, 3, 4, 5, 6).partition(_ < 3)
res25: (io.hascalator.data.List[Int], io.hascalator.data.List[Int]) = ([1, 2],[3, 4, 5, 6])
```

`filter`, applied to a predicate and a list, returns the list of those elements that satisfy the predicate; i.e.,

```scala
scala> List(1, 2, 3, 4, 5, 6).filter(_ < 3)
res26: io.hascalator.data.List[Int] = [1, 2]
```

### Zipping and unzipping lists

`zip` takes two lists and returns a list of corresponding pairs. If one input list is short, excess elements of the 
longer list are discarded.

```scala
scala> List(1, 2, 3, 4) zip List('a', 'b', 'c', 'd')
res27: io.hascalator.data.List[(Int, Char)] = [(1,a), (2,b), (3,c), (4,d)]
```
