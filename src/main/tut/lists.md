## `Data.List`

The `List` is an immutable, inductive data type defined either as 
  
  * the empty list `Nil`
  * the constructed list `Cons`, with an `head` and a `tail`
  
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
### Basic functions

`++` append two lists

```tut
xs ++ ys
```

`head`: extract the first element of a list, which must be non-empty.
 
```tut
xs.head
```

`tail`: extract the elements after the head of a list, which must be non-empty.
```tut
xs.tail
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

### List transformations

`xs.map(f) is the list obtained by applying `f` to each element of `xs`, i.e.,

```tut
xs.map(_ * 2)
```

`reverse` returns the elements of `xs` in reverse order. 

```tut
xs.reverse
```

### Sublists

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

### Zipping and unzipping lists

`zip` takes two lists and returns a list of corresponding pairs. If one input list is short, excess elements of the 
longer list are discarded.

```tut
List(1, 2, 3, 4) zip List('a', 'b', 'c', 'd')
```