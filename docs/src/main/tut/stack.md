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

`top` returns the top element for the `Stack` (if exits), without removing it.

```tut
stack.top
stack.push(42).top
```

`size` returns the number of elements contained in this `Stack`

```tut
stack.size
stack.push(1).push(2).size
```

`isEmpty` checks whether the current `Stack` is empty:

```tut
stack.isEmpty
stack.push(1).push(2).isEmpty
```

