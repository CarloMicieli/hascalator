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

package io.hascalator.data

/**
  * It represents a FIFO data structure, the first element
  * added to the queue will be the first one to be removed.
  *
  * @tparam A the `Queue` element type
  */
trait Queue[+A] {
  /**
    * Insert the new element to the last position of the `Queue`.
    *
    * @usecase def enqueue(el: A): Queue[A]
    * @inheritdoc
    * @param el the element to insert
    * @tparam A1 the element type
    * @return a new `Queue` with the element `el` inserted
    */
  def enqueue[A1 >: A](el: A1): Queue[A1]

  /**
    * Remove the element from the front position (if any).
    * @return
    */
  def dequeue: Either[EmptyQueueException, (A, Queue[A])]

  /**
    * Return the element in the front position, if exists.
    * @return optionally the front element
    */
  def peek: Maybe[A]

  /**
    * Check whether this `Queue` is empty.
    * @return `true` if this `Queue` is empty; `false` otherwise
    */
  def isEmpty: Boolean

  /**
    * Check whether this `Queue` is not empty.
    * @return `true` if this `Queue` is not empty; `false` otherwise
    */
  def nonEmpty: Boolean = !isEmpty

  /**
    * Return the current size of the `Queue`.
    * @return the number of elements in this `Queue`
    */
  def size: Int
}

object Queue {
  def empty[A]: Queue[A] = BalancedQueue.empty[A]
}

class EmptyQueueException extends Exception