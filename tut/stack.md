---
layout: default
title:  "Data.Stack"
---

# Data.Stack

It represents a LIFO data structure, the last element added to the stack will be the first one
to be removed.

```scala
import io.hascalator._
import Prelude._
import data.Stack
```

## Operations

```scala
scala> val stack = Stack.empty[Int]
stack: io.hascalator.data.Stack[io.hascalator.Prelude.Int] = <emptystack>
```

`push` creates a new `Stack` with the provided value `el` as its top element:

```scala
scala> stack.push(42)
res0: io.hascalator.data.Stack[Int] = <stack:top = 42>
```

`pop` If this `Stack` is not empty, it returns a pair with the top element and a new `Stack` without this
element; else it returns an `EmptyStackException` wrapped in a _Left_ value.

```scala
scala> stack.pop
res1: io.hascalator.data.Either[io.hascalator.data.EmptyStackException,(io.hascalator.Prelude.Int, io.hascalator.data.Stack[io.hascalator.Prelude.Int])] = Left(io.hascalator.data.ListStack$$anon$1: Stack is empty)

scala> stack.push(42).pop
res2: io.hascalator.data.Either[io.hascalator.data.EmptyStackException,(Int, io.hascalator.data.Stack[Int])] = Right((42,<emptystack>))
```

