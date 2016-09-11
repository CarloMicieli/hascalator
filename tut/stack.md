---
layout: default
title:  "Data.Stack"
---

# Data.Stack

A **Stack** is a collection of elements with two operations: `push`, which adds
elements to the collection and `pop` which removes the most recently added
element that was not yet removed.

The implementation provides other non-essential operations like `size`,
`isEmpty` and `top`. 

More generally, a `Stack` represents a LIFO data structure, where the last 
element added to the stack will be the first one to be removed.

## Performance

Both `pop` and `push` are constant time (`O(1)`) operations. The same is
valid for both `isEmpty` and `top`.
Due internal implementation, the `size` operation is linear in the number
of elements contained in the `Stack`.

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

`top` returns the top element for the `Stack` (if exits), without removing it.

```scala
scala> stack.top
res3: io.hascalator.data.Maybe[io.hascalator.Prelude.Int] = None

scala> stack.push(42).top
res4: io.hascalator.data.Maybe[Int] = Just(42)
```

`size` returns the number of elements contained in this `Stack`

```scala
scala> stack.size
res5: io.hascalator.Prelude.Int = 0

scala> stack.push(1).push(2).size
res6: io.hascalator.Prelude.Int = 2
```

`isEmpty` checks whether the current `Stack` is empty:

```scala
scala> stack.isEmpty
res7: io.hascalator.Prelude.Boolean = true

scala> stack.push(1).push(2).isEmpty
res8: io.hascalator.Prelude.Boolean = false
```

