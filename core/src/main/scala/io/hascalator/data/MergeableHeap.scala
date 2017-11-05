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

/** A mergeable heap (also called a ''meldable'' heap) is an abstract data type, which is a heap supporting
  * a merge operation.
  *
  * @tparam A the heap item type
  * @author Carlo Micieli
  * @since 0.0.1
  */
trait MergeableHeap[+A] extends Heap[A] {

  type MH[B] <: MergeableHeap[B]

  /** Combine the elements of `this` and `that` into a single heap.
    *
    * @usecase def merge(that: MergeableHeap[A]): MergeableHeap[A]
    * @param that the other heap
    * @param ord
    * @return
    */
  def merge[A1 >: A](that: MH[A1])(implicit ord: Ord[A1]): MH[A1]
}

object MergeableHeap {
  /** Create an empty mergeable heap.
    * @tparam A the key type
    * @return an empty mergeable Heap
    */
  def makeHeap[A: Ord]: MergeableHeap[A] = undefined

  /** Build a new mergeable heap from a list
    * @param xs a List
    * @tparam A the key type
    * @return a new Heap
    */
  def fromList[A: Ord](xs: List[A]): MergeableHeap[A] = undefined
}