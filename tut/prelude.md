## `Prelude`

The `Prelude` must imported to access the library types and functions.

```scala
import io.hascalator._
import Prelude._
```

`id`: the identity function, useful as placeholder when an high order
 function is needed but no meaningful option exists.

```scala
scala> id(42)
res0: Int = 42
```

```scala
scala> List(1, 2, 3).map(id)
res1: io.hascalator.data.List[Int] = [1, 2, 3]
```

`const`: is a unary function which evaluates to `x` for all inputs.

```scala
scala> List(0, 1, 2, 3).map(const(42))
res2: io.hascalator.data.List[Int] = [42, 42, 42, 42]
```

`flip`: takes its (first) two arguments in the reverse order of `f`.

```scala
scala> val f = (a: String, b: String) => a + " " + b
f: (io.hascalator.Prelude.String, io.hascalator.Prelude.String) => String = <function2>

scala> f("hello", "world")
res3: String = hello world

scala> f.flip("hello", "world")
res4: String = world hello
```

`until` yields the result of applying `f` until the predicate holds.

```scala
scala> until[Int](_ > 10)(_ + 1)(0)
res5: io.hascalator.Prelude.Int = 11
```
