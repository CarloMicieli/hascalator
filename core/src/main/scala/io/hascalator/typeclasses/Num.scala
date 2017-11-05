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
package typeclasses

import Prelude._
import scala.annotation.implicitNotFound
import scala.language.implicitConversions

/** It represents a basic typeclass for numeric types.
  * @tparam A the instance type
  * @author Carlo Micieli
  * @since 0.0.1
  */
@implicitNotFound("The type ${A} was not made instance of the Num type class")
trait Num[A] extends Any with Eq[A] with Show[A] {

  /** Conversion from an `Int`. An integer literal represents the application of the
    * function fromInteger to the appropriate value of type `Int`, so such literals
    * have type `A`, where `A` is an instance of the `Num` typeclass.
    *
    * @param n the `Int` value to convert
    * @return a new value of type `A`
    */
  def fromInteger(n: Integer): A

  /** The sum operation
    * @param x the first operand
    * @param y the second operand
    * @return the sum of `x` and `y`
    */
  def add(x: A, y: A): A

  /** The subtraction operation
    * @param x the first operand
    * @param y the second operand
    * @return the subtraction of `x` and `y`
    */
  def sub(x: A, y: A): A = add(x, negate(y))

  /** The multiplication operation
    * @param x the first operand
    * @param y the second operand
    * @return the multiplication of `x` and `y`
    */
  def mul(x: A, y: A): A

  /** The unary negation
    * @param x the operand
    * @return x negated
    */
  def negate(x: A): A

  /** Returns the absolute value
    * @param x the operand
    * @return the absolute value of `x`
    */
  def abs(x: A): A = mul(x, signum(x))

  /** Returns the sign of a number.
    *
    * The functions abs and signum should satisfy the law:
    * {{{
    * abs x * signum x == x
    * }}}
    * For real numbers, the signum is either -1 (negative), 0 (zero) or 1 (positive).
    *
    * @param x the number
    * @return the sign of `x`
    */
  def signum(x: A): A
}

object Num {
  def apply[A](implicit n: Num[A]): Num[A] = n

  implicit def fromFractional[A: Fractional]: Num[A] = apply[A]
  implicit def fromIntegral[A: Integral]: Num[A] = apply[A]

  trait NumOps[A] {
    def self: A
    def numInstance: Num[A]
    def +(other: A): A = numInstance.add(self, other)
    def -(other: A): A = numInstance.sub(self, other)
    def *(other: A): A = numInstance.mul(self, other)
    def abs: A = numInstance.abs(self)
    def negate: A = numInstance.negate(self)
    def signum: A = numInstance.signum(self)
  }

  object ops {
    implicit def toNumOps[A: Num](x: A): NumOps[A] = new NumOps[A] {
      override def self: A = x
      override def numInstance: Num[A] = implicitly[Num[A]]
    }
  }
}

protected[typeclasses] trait AdditionLaws {
  import Num.ops._

  def commutativityLaw[A: Num](a: A, b: A): Boolean = (a + b) == (b + a)
  def associativityLaw[A: Num](a: A, b: A, c: A): Boolean = ((a + b) + c) == (a + (b + c))

  def identityElement[A: Num](a: A): Boolean = {
    val id = implicitly[Num[A]].fromInteger(0)
    a + id == id + a
  }
}

protected[typeclasses] trait MultiplicationLaws {
  import Num.ops._

  def commutativityLaw[A: Num](a: A, b: A): Boolean = (a * b) == (b * a)
  def associativityLaw[A: Num](a: A, b: A, c: A): Boolean = ((a * b) * c) == (a * (b * c))

  def distributiveLaw[A: Num](a: A, b: A, c: A): Boolean = {
    a * (b + c) == a * b + a * c
  }

  def identityElement[A: Num](a: A): Boolean = {
    val id = implicitly[Num[A]].fromInteger(1)
    a * id == id * a
  }
}

protected[typeclasses] trait SignumLaws {
  import Num.ops._
  def law[A: Num](x: A): Boolean = {
    x.abs * x.signum == x
  }
}