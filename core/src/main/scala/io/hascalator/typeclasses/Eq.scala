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
import scala.annotation.implicitNotFound
import scala.language.implicitConversions

/** Eq is used for types that support equality and inequality testing.
  * The functions its members implement are `eq` and `neq`.
  *
  * @tparam A the instance data type
  * @author Carlo Micieli
  * @since 0.0.1
  */
@implicitNotFound("The type ${A} was not made an instance of the Eq type class")
trait Eq[A] extends Any {
  /** Checks whether lhs and rhs are equals
    * @param lhs the first value
    * @param rhs the second value
    * @return `true` if they are equals; `false` otherwise
    */
  def eq(lhs: A, rhs: A): Boolean

  /** Checks whether lhs and rhs are different
    * @param lhs the first value
    * @param rhs the second value
    * @return `true` if they are equals; `false` otherwise
    */
  def neq(lhs: A, rhs: A): Boolean = !eq(lhs, rhs)
}

object Eq {
  def apply[A](implicit ev: Eq[A]): Eq[A] = ev

  def fromFunction[A](comp: (A, A) => Boolean): Eq[A] = (lhs: A, rhs: A) => comp(lhs, rhs)

  trait EqOps[A] {
    def self: A
    def eqInstance: Eq[A]
    def ===(that: A): Boolean = eqInstance.eq(self, that)
    def =/=(that: A): Boolean = eqInstance.neq(self, that)
  }

  object ops {
    implicit def toEqOp[A](x: A)(implicit ev: Eq[A]) = new EqOps[A] {
      override def self: A = x
      override def eqInstance: Eq[A] = ev
    }
  }

  implicit val floatEq: Eq[Float] = fromFunction(_ equals _)
  implicit val doubleEq: Eq[Double] = fromFunction(_ equals _)
  implicit val shortEq: Eq[Short] = fromFunction(_ equals _)
  implicit val byteEq: Eq[Byte] = fromFunction(_ equals _)
  implicit val intEq: Eq[Int] = fromFunction(_ equals _)
  implicit val longEq: Eq[Long] = fromFunction(_ equals _)

  implicit val booleanEq: Eq[Boolean] = fromFunction(_ equals _)

  implicit val charEq: Eq[Char] = fromFunction(_ equals _)
  implicit val stringEq: Eq[String] = fromFunction(_ equals _)
}

/** Laws Eq instances should have are the following:
  * - Reflexivity: x == x should always be True.
  * - Symmetry: x == y iff y == x.
  * - Transitivity: If x == y and y == z, then x == z.
  * - Substitution: If x == y, then f x == f y for all f.
  */
trait EqLaws {
  def reflexivityLaw[A: Eq](x: A): Boolean = {
    Eq[A].eq(x, x)
  }

  def symmetryLaw[A: Eq](x: A, y: A): Boolean = {
    Eq[A].eq(x, y) == Eq[A].eq(y, x)
  }

  def transitivityLaw[A: Eq](x: A, y: A, z: A): Boolean = {
    if (Eq[A].eq(x, y) && Eq[A].eq(y, z)) {
      Eq[A].eq(x, z)
    } else {
      true
    }
  }

  def substitutionLaw[A: Eq](x: A, y: A)(f: A => A): Boolean = {
    Eq[A].eq(x, y) == Eq[A].eq(f(x), f(y))
  }
}