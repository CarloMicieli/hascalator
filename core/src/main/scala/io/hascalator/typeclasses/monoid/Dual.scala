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
package monoid

import Prelude._

/** The dual of a [[Monoid]], obtained by swapping the arguments of `mappend`.
  *
  * @tparam A
  * @author Carlo Micieli
  * @since 0.1
  */
final case class Dual[A](getDual: A) extends AnyVal

object Dual {

  implicit def toEq[A: Eq]: Eq[Dual[A]] = (lhs: Dual[A], rhs: Dual[A]) => {
    Eq[A].eq(lhs.getDual, rhs.getDual)
  }

  implicit def toShow[A: Show]: Show[Dual[A]] = (x: Dual[A]) => {
    val v = Show[A].show(x.getDual)
    s"Dual {getDual = $v}"
  }

  implicit def toMonoid[A: Monoid]: Monoid[Dual[A]] = new Monoid[Dual[A]] {
    private val monoidA = Monoid[A]

    override def mempty: Dual[A] = Dual(monoidA.mempty)

    override def mappend(dx: Dual[A], dy: Dual[A]): Dual[A] = {
      Dual(monoidA.mappend(dy.getDual, dx.getDual))
    }
  }
}
