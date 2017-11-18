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

import Prelude._

/** A heap is a data structure that satisfies the following ''heap property'':
  *
  * - `min` operation always returns the minimum element;
  * - `extractMin` operation removes the top element from the heap while the heap property should
  *   be kept, so that the new top element is still the minimum one;
  * - insert a new element to heap should keep the heap property. That the new top is still
  *  the minimum (maximum) element;
  * - other operations including merge etc should all keep the heap property.
  *
  * @tparam A the heap item type
  * @author Carlo Micieli
  * @since 0.0.1
  */
trait Heap[+A] extends Any {
  self =>

  /** Returns the number of items in this heap
    * @return the number of items
    */
  def size: Int

  /** Returns true when this Heap is empty, false otherwise
    * @return true if empty
    */
  def isEmpty: Boolean

  /** Insert a new key.
    *
    * @usecase def insert(k: A): Heap[A]
    * @param k the new key
    * @param ord
    * @tparam A1
    * @return a new Heap with the new key
    */
  def insert[A1 >: A](k: A1)(implicit ord: Ord[A1]): Heap[A1]

  /** Return the minimum element, or `None` if no such element exists.
    * @return the minimum key
    */
  def min: Maybe[A]

  /** Find and remove the minimum element in this Heap.
    *
    * Because this operation must preserve the __heap property__ is bound to O(lg n).
    *
    * @usecase def extractMin: Either[EmptyHeapException, (A, Heap[A])]
    * @tparam A1 the key type
    * @return the minimum key (if any)
    */
  def extractMin[A1 >: A](implicit ord: Ord[A1]): Either[EmptyHeapException, (A1, Heap[A1])]
}

object Heap {
  /** Create an empty mergeable heap.
    * @tparam A the key type
    * @return an empty mergeable Heap
    */
  def makeHeap[A: Ord]: Heap[A] = undefined

  /** Build a new heap from a list
    * @param xs a List
    * @tparam A the key type
    * @return a new Heap
    */
  def fromList[A: Ord](xs: List[A]): Heap[A] = undefined
}

class EmptyHeapException extends java.lang.Exception("This Heap is empty")