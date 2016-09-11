---
layout: default
title:  "Data.Queue"
---

# Data.Queue

It represents a FIFO data structure, the first element added to the queue will be the first one to be removed.

```tut:silent
import io.hascalator._
import Prelude._
import data.Queue
```

## Operations

```tut
val queue = Queue.empty[Int]
```

`enqueue` inserts the new element to the last position of the `Queue`

```tut
val q2 = queue.enqueue(42).enqueue(41)
q2.peek
```

`dequeue` removes the element from the front position (if any).

```tut
val q3 = queue.enqueue(42).enqueue(41)
q3.dequeue
```

Returns a ''Left'' when the queue is empty:

```tut
queue.dequeue
```

`peek` return the element in the front position, if exists.

```tut
queue.enqueue(42).peek
queue.peek
```

`size` returns the current size of the `Queue`.

```tut
queue.enqueue(42).size
queue.size
```

`isEmpty` checks whether this `Queue` is empty.

```tut
queue.enqueue(42).isEmpty
queue.isEmpty
```