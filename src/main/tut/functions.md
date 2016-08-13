## Functions

`const` returns the constant function which produce an unary function that produce always the same value.

```tut:silent
import io.hascalator.functions._
```

```tut
val f: String => Int = const(42)
f("hello")
f("world")
```