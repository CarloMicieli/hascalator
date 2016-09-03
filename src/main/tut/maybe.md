## Maybe

The Maybe type encapsulates an optional value. A value of type Maybe a either contains a value 
of type a (represented as Just a), or it is empty (represented as Nothing). Using Maybe is a 
good way to deal with errors or exceptional cases without resorting to drastic measures 
such as error.

The Maybe type is also a monad. It is a simple kind of error monad, where all errors are 
represented by Nothing. A richer error monad can be built using the Either type.

```tut:silent
import io.hascalator._
import Prelude._
```

To create new values, the type `Maybe` provides two functions:

```tut
Maybe.just(42)
Maybe.none[Int]
```

Even if its use is discouraged, the function `get` allows to extract the wrapped value. This is not
a total function, and it will fail for `None` values.

This is fine:

```tut
Maybe.just(42).get
```

while this will fail:

```tut:fail
Maybe.none[Int].get
```

The best way to use `Maybe` values is through the combinators:

```tut
Maybe.just(42).map(_ * 2)
```


