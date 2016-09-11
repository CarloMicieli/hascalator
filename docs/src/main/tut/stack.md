---
layout: default
title:  "Data.Stack"
---

# Data.Stack

It represents a LIFO data structure, the last element added to the stack will be the first one
to be removed.

```tut:silent
import io.hascalator._
import Prelude._
import data.Stack
```

## Operations

```tut
val stack = Stack.empty[Int]
```

`push` creates a new `Stack` with the provided value `el` as its top element:

```tut
stack.push(42)
```

`pop` If this `Stack` is not empty, it returns a pair with the top element and a new `Stack` without this
element; else it returns an `EmptyStackException` wrapped in a _Left_ value.

```tut
stack.pop
stack.push(42).pop
```

