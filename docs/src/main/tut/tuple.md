---
layout: default
title:  "Data.Tuple"
---

# Data.Tuple

The tuple data types, and associated functions.

```tut:silent
import io.hascalator._
import Prelude._
import data.Tuple._
```

```tut
val pair = ("answer", 42)
```

`fst`: Extract the first component of a pair.

```tut
fst(pair)
```

`snd`: Extract the second component of a pair.

```tut
snd(pair)
```

`curry`: converts an uncurried function to a curried function.

```tut
val f: ((String, Int)) => String = { case (s, n) => s"$s($n)" }
val g = curry(f)

f(pair)
g(fst(pair))(snd(pair))
```

`uncurry`: converts a curried function to a function on pairs.

```tut
val f: String => Int => String = n => s => s"$s($n)"
val g = uncurry(f)

f(fst(pair))(snd(pair))
g(pair)
```

`swap` swap the components of a pair.

```tut
swap(pair)
```
