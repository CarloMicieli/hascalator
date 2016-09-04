## `Prelude`

The `Prelude` must imported to access the library types and functions.

```tut:silent
import io.hascalator._
import Prelude._
```

`id`: the identity function, useful as placeholder when an high order
 function is needed but no meaningful option exists.

```tut
id(42)
```

```tut
List(1, 2, 3).map(id)
```

`const`: is a unary function which evaluates to `x` for all inputs.

```tut
List(0, 1, 2, 3).map(const(42))
```

`flip`: takes its (first) two arguments in the reverse order of `f`.

```tut
val f = (a: String, b: String) => a + " " + b
f("hello", "world")
f.flip("hello", "world")
```

`until` yields the result of applying `f` until the predicate holds.

```tut
until[Int](_ > 10)(_ + 1)(0)
```
