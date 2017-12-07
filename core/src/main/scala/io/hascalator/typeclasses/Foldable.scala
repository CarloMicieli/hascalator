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
package typeclasses

import Prelude._
//import monoid.{Endo, Dual}

import scala.annotation.implicitNotFound

/** Class of data structures that can be folded to a summary value.
  *
  * @tparam T the instance type
  * @author Carlo Micieli
  * @since 0.1
  */
@implicitNotFound("The type ${T} was not made instance of the Foldable type class")
trait Foldable[T[_]] extends Any {

  /** Right-associative fold of a structure.
    * @param t
    * @param z
    * @param f
    * @tparam A
    * @tparam B
    * @return
    */
  def foldRight[A, B](t: T[A])(z: B)(f: (A, B) => B): B

  /** Left-associative fold of a structure.
    *
    * @param t
    * @param z
    * @param f
    * @tparam A
    * @tparam B
    * @return
    */
  def foldLeft[A, B](t: T[A])(z: B)(f: (B, A) => B): B

  /** Map each element of the structure to a monoid,  and combine the results.
    *
    * @param t
    * @param f
    * @tparam A
    * @tparam M
    * @return
    */
  def foldMap[A, M: Monoid](t: T[A])(f: A => M): M = {
    val m = implicitly[Monoid[M]]
    foldRight(t)(m.mempty)((a, b) => m.mappend(f(a), b))
  }

}
