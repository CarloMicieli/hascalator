---
layout: default
title:  "Data.Maybe"
---

## `Data.Maybe`

The Maybe type encapsulates an optional value. A value of type Maybe a either contains a value
of type a (represented as Just a), or it is empty (represented as Nothing). Using Maybe is a
good way to deal with errors or exceptional cases without resorting to drastic measures
such as error.

The Maybe type is also a monad. It is a simple kind of error monad, where all errors are
represented by Nothing. A richer error monad can be built using the Either type.

```scala
import io.hascalator._
import Prelude._
```

To create new values, the type `Maybe` provides two functions:

```scala
scala> Maybe.just(42)
res0: io.hascalator.data.Maybe[Int] = Just(42)

scala> Maybe.none[Int]
res1: io.hascalator.data.Maybe[io.hascalator.Prelude.Int] = None
```

Even if its use is discouraged, the function `get` allows to extract the wrapped value. This is not
a total function, and it will fail for `None` values.

This is fine:

```scala
scala> Maybe.just(42).get
res2: Int = 42
```

while this will fail:

```scala
scala> Maybe.none[Int].get
io.hascalator.ApplicationException: *** Exception: Maybe.get: a value doesn't exist
  at io.hascalator.Prelude$.error(Prelude.scala:159)
  at io.hascalator.data.None$.get(Maybe.scala:305)
  at io.hascalator.data.None$.get(Maybe.scala:304)
  ... 282 elided
```

The best way to use `Maybe` values is through the combinators:

```scala
scala> Maybe.just(42).map(_ * 2)
res4: io.hascalator.data.Maybe[Int] = Just(84)
```
