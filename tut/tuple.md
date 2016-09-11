---
layout: default
title:  "Data.Tuple"
---

# Tuple

The tuple data types, and associated functions.

```scala
import io.hascalator._
import Prelude._
import data.Tuple._
```

```scala
scala> val pair = ("answer", 42)
pair: (String, Int) = (answer,42)
```

`fst`: Extract the first component of a pair.

```scala
scala> fst(pair)
res0: String = answer
```

`snd`: Extract the second component of a pair.

```scala
scala> snd(pair)
res1: Int = 42
```

`curry`: converts an uncurried function to a curried function.

```scala
scala> val f: ((String, Int)) => String = { case (s, n) => s"$s($n)" }
f: ((io.hascalator.Prelude.String, io.hascalator.Prelude.Int)) => io.hascalator.Prelude.String = <function1>

scala> val g = curry(f)
g: io.hascalator.Prelude.String => (io.hascalator.Prelude.Int => io.hascalator.Prelude.String) = <function1>

scala> f(pair)
res2: io.hascalator.Prelude.String = answer(42)

scala> g(fst(pair))(snd(pair))
res3: io.hascalator.Prelude.String = answer(42)
```

`uncurry`: converts a curried function to a function on pairs.

```scala
scala> val f: String => Int => String = n => s => s"$s($n)"
f: io.hascalator.Prelude.String => (io.hascalator.Prelude.Int => io.hascalator.Prelude.String) = <function1>

scala> val g = uncurry(f)
g: ((io.hascalator.Prelude.String, io.hascalator.Prelude.Int)) => io.hascalator.Prelude.String = <function1>

scala> f(fst(pair))(snd(pair))
res4: io.hascalator.Prelude.String = 42(answer)

scala> g(pair)
res5: io.hascalator.Prelude.String = 42(answer)
```

`swap` swap the components of a pair.

```scala
scala> swap(pair)
res6: (Int, String) = (42,answer)
```
