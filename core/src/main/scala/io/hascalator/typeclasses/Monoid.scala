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
import simulacrum.typeclass

import scala.annotation.implicitNotFound
import scala.language.implicitConversions

/** The class of monoids (types with an associative binary operation that has an identity). Instances should satisfy
  * the following laws:
  *
  * - `mappend mempty x = x`
  * - `mappend x mempty = x`
  * - `mappend x (mappend y z) = mappend (mappend x y) z`
  * - `mconcat = foldr mappend mempty`
  *
  * The method names refer to the monoid of lists under concatenation, but there are many other instances.
  *
  * @tparam A the instance type
  * @author Carlo Micieli
  * @since 0.1
  */
@implicitNotFound("The type ${A} was not made instance of the Monoid type class")
@typeclass trait Monoid[A] extends Semigroup[A] {
  /** The identity element
    * @return the identity element
    */
  def mempty: A

  def concat(xs: List[A])(implicit ma: Monoid[A]): A = {
    xs.foldRight(ma.mempty)(ma.mappend)
  }

  override def sTimes(times: Int)(z: A): A = {
    if (times == 0) {
      mempty
    } else {
      super.sTimes(times)(z)
    }
  }
}

object Monoid {
  implicit def listMonoid[A]: Monoid[List[A]] = new Monoid[List[A]] {
    override def mappend(x: List[A], y: List[A]): List[A] = x append y
    override def mempty: List[A] = List.empty[A]
  }
}