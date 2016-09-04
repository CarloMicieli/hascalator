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
import scala.annotation.implicitNotFound

/** The `Ord` typeclass is used for totally ordered data types.
  *
  * Instances of `Ord` can be derived for any user-defined data type whose constituent types
  * are in `Ord`. The declared order of the constructors in the data declaration determines the
  * ordering in derived Ord instances. The Ordering data type allows a single comparison to
  * determine the precise ordering of two objects.
  *
  * Minimal complete definition is `compare`.
  *
  * ==Laws==
  * A good implementation for an instance of `Ord` should respect the following laws:
  * - If `x ≤ y` and `y ≤ x` then `x = y` (''antisymmetry'');
  * - If `x ≤ y` and `y ≤ z` then `x ≤ z` (''transitivity'');
  * - `x ≤ y` or `y ≤ z` (''totality'').
  *
  * @tparam A the instance data type
  * @author Carlo Micieli
  * @since 0.0.1
  */
@implicitNotFound("The type ${A} was not made an instance of the Ord type class")
trait Ord[A] extends Any with Eq[A] { self =>
  /** Returns an `Ordering` value that communicates how `x` compares to `y`.
    * @param x the first value
    * @param y the second value
    * @return an `Ordering` value
    */
  def compare(x: A, y: A): Ordering

  /** Returns `true` if `x < y` in the ordering
    * @param x the first value
    * @param y the second value
    * @return `true` if `x < y` in the ordering
    */
  def lt(x: A, y: A): Boolean = compare(x, y) == Ordering.LT

  /** Returns `true` if `x <= y` in the ordering
    * @param x the first value
    * @param y the second value
    * @return `true` if `x <= y` in the ordering
    */
  def lte(x: A, y: A): Boolean = {
    val cmp = compare(x, y)
    cmp == Ordering.EQ || cmp == Ordering.LT
  }

  /** Returns `true` if `x > y` in the ordering
    * @param x the first value
    * @param y the second value
    * @return `true` if `x > y` in the ordering
    */
  def gt(x: A, y: A): Boolean = compare(x, y) == Ordering.GT

  /** Returns `true` if `x >= y` in the ordering
    * @param x the first value
    * @param y the second value
    * @return `true` if `x >= y` in the ordering
    */
  def gte(x: A, y: A): Boolean = {
    val cmp = compare(x, y)
    cmp == Ordering.EQ || cmp == Ordering.GT
  }

  /** Return `x` if `x <= y`, otherwise `y`.
    * @param x the first value
    * @param y the second value
    * @return the min value between `x` and `y`
    */
  def min(x: A, y: A): A = {
    if (lte(x, y)) {
      x
    } else {
      y
    }
  }

  /** Return `x` if `x >= y`, otherwise `y`.
    * @param x the first value
    * @param y the second value
    * @return the max value between `x` and `y`
    */
  def max(x: A, y: A): A = {
    if (gte(x, y)) {
      x
    } else {
      y
    }
  }
}

object Ord {
  def apply[A](implicit o: Ord[A]): Ord[A] = o

  def apply[A](cmp: (A, A) => Ordering): Ord[A] = new Ord[A] {
    override def compare(lhs: A, rhs: A): Ordering = cmp(lhs, rhs)
    override def eq(lhs: A, rhs: A): Boolean = compare(lhs, rhs) == Ordering.EQ
  }

  trait OrdOps[A] {
    def self: A

    def ordInstance: Ord[A]

    def ===(that: A): Boolean = ordInstance.eq(self, that)
    def =/=(that: A): Boolean = ordInstance.neq(self, that)
    def <(that: A): Boolean = ordInstance.lt(self, that)
    def <=(that: A): Boolean = ordInstance.lte(self, that)
    def >(that: A): Boolean = ordInstance.gt(self, that)
    def >=(that: A): Boolean = ordInstance.gte(self, that)
    def max(that: A): A = ordInstance.max(self, that)
    def min(that: A): A = ordInstance.min(self, that)
  }

  object ops {
    implicit def toOrdOps[A: Ord](x: A): OrdOps[A] = new OrdOps[A] {
      override def self: A = x

      override def ordInstance: Ord[A] = implicitly[Ord[A]]
    }
  }

  implicit val booleanOrd: Ord[Boolean] = fromCompare((x, y) => java.lang.Boolean.compare(x, y))
  implicit val byteOrd: Ord[Byte] = fromCompare((x, y) => java.lang.Byte.compare(x, y))
  implicit val shortOrd: Ord[Short] = fromCompare((x, y) => java.lang.Short.compare(x, y))
  implicit val intOrd: Ord[Int] = fromCompare((x, y) => java.lang.Integer.compare(x, y))
  implicit val longOrd: Ord[Long] = fromCompare((x, y) => java.lang.Long.compare(x, y))
  implicit val floatOrd: Ord[Float] = fromCompare((x, y) => java.lang.Float.compare(x, y))
  implicit val doubleOrd: Ord[Double] = fromCompare((x, y) => java.lang.Double.compare(x, y))
  implicit val charOrd: Ord[Char] = fromCompare((x, y) => java.lang.Character.compare(x, y))
  implicit val stringOrd: Ord[String] = fromCompare((x, y) => x.compareTo(y))

  private def fromCompare[A: Eq](cmp: (A, A) => Int): Ord[A] = Ord(Ordering(cmp))
}

protected[typeclasses] trait OrdLaws {
  import Ord.ops._

  def antisymmetryLaw[A: Ord](x: A, y: A): Boolean = {
    if (x <= y && y <= x) {
      implicitly[Ord[A]].eq(x, y)
    } else {
      true
    }
  }

  def transitivityLaw[A: Ord](x: A, y: A, z: A): Boolean = {
    if (x <= y && y <= z) {
      x <= z
    } else {
      true
    }
  }

  def totalityLaw[A: Ord](x: A, y: A): Boolean = {
    x <= y || y <= x
  }
}