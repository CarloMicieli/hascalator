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

import Prelude._

/** The result of one `view` operation on a `FingerTree`, which takes one element off the end
  * of the finger tree and returns that element along with a finger tree representing the rest of
  * the elements.
  *
  * @tparam A
  * @author Carlo Micieli
  * @since 0.1
  */
private[fingertrees] sealed trait View[+V, +A] extends Any

private[fingertrees] case object Nil extends View[Nothing, Nothing]
private[fingertrees] case class TView[V, A](a: A, ft: FingerTree[V, A]) extends View[V, A]

