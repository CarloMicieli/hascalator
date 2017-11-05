/*
 * Copyright 2016 CarloMicieli
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

import Prelude._

/** A double-ended queue.
  *
  * @tparam A the deque type
  * @author Carlo Micieli
  * @since 0.0.1
  */
trait Dequeue[A] extends Any {
  /** Returns True if this dequeue is empty.
    * @return True if empty; False otherwise
    */
  def isEmpty: Boolean

  /** Returns the number of elements in this dequeue.
    * @return the number of elements
    */
  def length: Int

  /** Returns the item on the front of the dequeue.
    * @return the first item
    */
  def first: Maybe[A]

  /** Returns the item on the end of the dequeue.
    * @return the last item
    */
  def last: Maybe[A]

  /** Reverses the dequeue.
    * @return the reverse
    */
  def reverse: Dequeue[A]

  /** Pushes an item onto the front of the dequeue.
    * @param x the new first element
    * @return a modified dequeue
    */
  def pushFront(x: A): Dequeue[A]

  /** Pops an item from the front of the dequeue.
    *
    * Potentially it throws a UnbalancedDequeueException when this deque is far too unbalanced.
    *
    * @return a pair of the first element and the new dequeue
    */
  def popFront: Maybe[(A, Dequeue[A])]

  /** Pushes an item onto the back of the dequeue.
    * @param x the new last item
    * @return the modified dequeue
    */
  def pushBack(x: A): Dequeue[A]

  /** Pops an item from the back of the dequeue.
    * @return a pair of the last element and the new dequeue
    */
  def popBack: Maybe[(A, Dequeue[A])]

  /** Returns the first n items from the front of the queue, in the order they would be popped.
    * @param n the number of items to take
    * @return a list of the first n elements
    */
  def takeFront(n: Int): List[A]

  /** Returns the last n items from the end of the queue, in the order they would be popped.
    * @param n the number of items to take
    * @return a list of the last n elements
    */
  def takeBack(n: Int): List[A]

  /** Returns a list with the dequeue items
    * @return the items list
    */
  def toList: List[A]
}
