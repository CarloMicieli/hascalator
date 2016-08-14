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

package io.hascalator.typeclasses

import scala.language.implicitConversions
import scala.annotation.implicitNotFound

/**
  * The Ord class is used for totally ordered datatypes.
  *
  * Instances of Ord can be derived for any user-defined datatype whose constituent types
  * are in Ord. The declared order of the constructors in the data declaration determines the
  * ordering in derived Ord instances. The Ordering data type allows a single comparison to
  * determine the precise ordering of two objects.
  *
  * Minimal complete definition: either compare or <=. Using compare can be more efficient
  * for complex types.
  *
  * @tparam A
  */
@implicitNotFound("The type ${A} was not made an instance of the Ord type class")
trait Ord[A] extends Eq[A] { self =>
  def compare(x: A, y: A): Ordering
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

    def ===(that: A): Boolean = ordInstance.compare(self, that) == Ordering.EQ
    def =/=(that: A): Boolean = ordInstance.compare(self, that) != Ordering.EQ
    def <(that: A): Boolean = ordInstance.compare(self, that) == Ordering.LT

    def <=(that: A): Boolean = {
      val res = ordInstance.compare(self, that)
      res == Ordering.LT || res == Ordering.EQ
    }

    def >(that: A): Boolean = ordInstance.compare(self, that) == Ordering.GT

    def >=(that: A): Boolean = {
      val res = ordInstance.compare(self, that)
      res == Ordering.GT || res == Ordering.EQ
    }
  }

  object ops {
    implicit def toOrdOps[A: Ord](x: A): OrdOps[A] = new OrdOps[A] {
      override def self: A = x

      override def ordInstance: Ord[A] = implicitly[Ord[A]]
    }
  }

  implicit val booleanOrd: Ord[Boolean] = fromCompare((x, y) => x compare y)
  implicit val byteOrd: Ord[Byte] = fromCompare((x, y) => x compare y)
  implicit val shortOrd: Ord[Short] = fromCompare((x, y) => x compare y)
  implicit val intOrd: Ord[Int] = fromCompare((x, y) => x compare y)
  implicit val longOrd: Ord[Long] = fromCompare((x, y) => x compare y)
  implicit val floatOrd: Ord[Float] = fromCompare((x, y) => x compare y)
  implicit val doubleOrd: Ord[Double] = fromCompare((x, y) => x compare y)
  implicit val charOrd: Ord[Char] = fromCompare((x, y) => x compare y)
  implicit val stringOrd: Ord[String] = fromCompare((x, y) => x compare y)

  private def fromCompare[A: Eq](cmp: (A, A) => Int): Ord[A] = Ord(Ordering(cmp))
}

sealed trait Ordering

object Ordering {
  case object LT extends Ordering
  case object EQ extends Ordering
  case object GT extends Ordering

  def apply(i: Int): Ordering = {
    i match {
      case 0          => Ordering.EQ
      case n if n < 0 => Ordering.LT
      case n if n > 0 => Ordering.GT
    }
  }

  def apply[A](cmp: (A, A) => Int): (A, A) => Ordering = {
    (x, y) => Ordering(cmp(x, y))
  }
}

/**
  *
  *  - If x ≤ y and y ≤ x then x = y (antisymmetry);
  *  - If x ≤ y and y ≤ z then x ≤ z (transitivity);
  *  - x ≤ y or y ≤ z (totality).
  */
trait OrdLaws {
  import Ord.ops._

  def antisymmetryLaw[A: Ord](x: A, y: A): Boolean = {
    if (x <= y && y <= x) {
      implicitly[Ord[A]].compare(x, y) == Ordering.EQ
    } else {
      true
    }
  }

  def transitivityLaw[A: Ord](x: A, y: A, z: A): Boolean = {
    if (x <= y && y <= z) x <= z else true
  }

  def totalityLaw[A: Ord](x: A, y: A): Boolean = {
    x <= y || y <= x
  }
}