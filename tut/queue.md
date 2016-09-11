---
layout: default
title:  "Data.Queue"
---

# Data.Queue

It represents a FIFO data structure, the first element added to the queue will be the first one to be removed.

```scala
import io.hascalator._
import Prelude._
import data.Queue
```

## Operations

```scala
scala> val queue = Queue.empty[Int]
queue: io.hascalator.data.Queue[io.hascalator.Prelude.Int] = io.hascalator.data.BalancedQueue@68e06296
```

`enqueue` inserts the new element to the last position of the `Queue`

```scala
scala> val q2 = queue.enqueue(42).enqueue(41)
q2: io.hascalator.data.Queue[Int] = io.hascalator.data.BalancedQueue@15ff8008

scala> q2.peek
res0: io.hascalator.data.Maybe[Int] = Just(42)
```

`dequeue` removes the element from the front position (if any).

```scala
scala> val q3 = queue.enqueue(42).enqueue(41)
q3: io.hascalator.data.Queue[Int] = io.hascalator.data.BalancedQueue@49c283ac

scala> q3.dequeue
res1: io.hascalator.data.Either[io.hascalator.data.EmptyQueueException,(Int, io.hascalator.data.Queue[Int])] = Right((42,io.hascalator.data.BalancedQueue@319b9007))
```

Returns a ''Left'' when the queue is empty:

```scala
scala> queue.dequeue
res2: io.hascalator.data.Either[io.hascalator.data.EmptyQueueException,(io.hascalator.Prelude.Int, io.hascalator.data.Queue[io.hascalator.Prelude.Int])] = Left(io.hascalator.data.BalancedQueue$$anon$1)
```

`peek` return the element in the front position, if exists.

```scala
scala> queue.enqueue(42).peek
res3: io.hascalator.data.Maybe[Int] = Just(42)

scala> queue.peek
res4: io.hascalator.data.Maybe[io.hascalator.Prelude.Int] = None
```

`size` returns the current size of the `Queue`.

```scala
scala> queue.enqueue(42).size
res5: io.hascalator.Prelude.Int = 1

scala> queue.size
res6: io.hascalator.Prelude.Int = 0
```

`isEmpty` checks whether this `Queue` is empty.

```scala
scala> queue.enqueue(42).isEmpty
res7: io.hascalator.Prelude.Boolean = false

scala> queue.isEmpty
res8: io.hascalator.Prelude.Boolean = true
```
