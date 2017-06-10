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
import scala.language.implicitConversions

/** The class of monoids (types with an associative binary operation that has an identity). Instances should satisfy
  * the following laws:
  *
  *
  * @tparam A the instance type
  * @author Carlo Micieli
  * @since 0.0.1
  */
trait Monoid[A] extends Semigroup[A] {
  /** The identity element
    * @return the identity element
    */
  def mIdentity: A

  override def sTimes(times: Int)(z: A): A = {
    if (times == 0) {
      mIdentity
    } else {
      super.sTimes(times)(z)
    }
  }
}

object Monoid {
  def apply[A](implicit sg: Monoid[A]): Monoid[A] = sg

  trait MonoidOps[A] {
    def self: A
    def monoidInstance: Monoid[A]

    def <>(that: A): A = monoidInstance.mAppend(self, that)
  }

  object ops {
    implicit def toMonoidOps[A: Monoid](x: A): MonoidOps[A] = new MonoidOps[A] {
      override def self: A = x
      override def monoidInstance: Monoid[A] = implicitly[Monoid[A]]
    }
  }

  implicit def listMonoid[A]: Monoid[List[A]] = new Monoid[List[A]] {
    override def mAppend(x: List[A], y: List[A]): List[A] = x append y
    override def mIdentity: List[A] = List.empty[A]
  }
}