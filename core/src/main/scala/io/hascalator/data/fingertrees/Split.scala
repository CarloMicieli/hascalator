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
package fingertrees

/** A data structure to store the split location that our search found; the left and right parts of the sequence
  * are stored as finger trees, and the element `x` (which caused predicate to switch) is stored directly.
  *
  * @param left
  * @param x
  * @param right
  * @tparam V
  * @tparam A
  */
private[fingertrees] final case class Split[V, A](left: FingerTree[V, A], x: A, right: FingerTree[V, A])