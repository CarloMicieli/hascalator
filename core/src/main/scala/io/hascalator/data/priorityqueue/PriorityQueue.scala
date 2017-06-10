/*
 * Copyright 2016 Carlo Micieli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.hascalator
package data
package priorityqueue

import Prelude._

/** It represents a priority queue, a data structure made of prioritized elements that allows
  * arbitrary element insertion, and allows the removal of the element that has first priority.
  * @tparam K the key type
  * @tparam V the value type
  *
  * @author Carlo Micieli
  * @since 0.0.1
  */
trait PriorityQueue[+K, +V] {
  /** Creates a new `PriorityQueue` with a new entry with key `key` and
    * value `value` in the priority queue.
    *
    * @usecase def insert(key: K, value: V): PriorityQueue[K, V]
    * @param key the key
    * @param value the value which corresponds to the key
    * @tparam K1 the key type
    * @tparam V1 the value type
    * @return a new `PriorityQueue` with the new element
    */
  def insert[K1 >: K, V1 >: V](key: K1, value: V1): PriorityQueue[K1, V1]

  /** Returns (but does not remove) a priority queue entry `(k, v)`
    * having minimal key; returns `None` if the priority queue is empty.
    * @return optionally the pair `(key, value)` for the element with the minimal key
    */
  def min: Maybe[(K, V)]

  /** Merge queues `this` and `that` into a single queue.
    *
    * @usecase def meld(that: PriorityQueue[K, V]): PriorityQueue[K, V]
    * @param that the second queue to be merged
    * @tparam K1 the key type
    * @tparam V1 the value type
    * @return a new PriorityQueue
    */
  def meld[K1 >: K, V1 >: V](that: PriorityQueue[K1, V1]): PriorityQueue[K1, V1]

  /** Returns a pair with the remove entry `(k, v)` having the minimal key and a new
    * priority queue without this entry. Return a `Bad` value whether this is empty.
    * @return a `Good` pair with the removed entry and the new priority queue; a `Bad` if this is empty
    */
  def removeMin: Either[((K, V), PriorityQueue[K, V]), EmptyPriorityQueueException]

  /** Returns the number of entries in this priority queue.
    * @return the number of entries
    */
  def size: Int

  /** Returns a `true` whether the priority queue is empty; `false` otherwise.
    * @return `true` if this is empty; `false` otherwise
    */
  def isEmpty: Boolean

  /** Returns a `true` whether the priority queue is not empty; `false` otherwise.
    * @return `true` if this is not empty; `false` otherwise
    */
  def nonEmpty: Boolean
}

sealed class EmptyPriorityQueueException extends Exception("PriorityQueue: this queue is empty")