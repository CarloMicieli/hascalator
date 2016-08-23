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

/**
  * It represents the type class for types that implement the numeric operation for
  * whole numbers.
  *
  * @tparam A the type class instance type
  * @author Carlo Micieli
  * @since 0.0.1
  */
@implicitNotFound("The type ${A} was not made an instance of the Integral type class")
trait Integral[A] extends Any with Num[A] {

  /**
    * Returns the integer division operation
    * @param a the first operand
    * @param b the second operand
    * @return the division
    */
  def div(a: A, b: A): A

  /**
    * Returns the integer remainder operation
    * @param a the first operand
    * @param b the second operand
    * @return the remainder
    */
  def mod(a: A, b: A): A

  /**
    * Returns the integer division and remainder operation
    * @param a the first operand
    * @param b the second operand
    * @return a pair with division and remainder
    */
  def divMod(a: A, b: A): (A, A) = (div(a, b), mod(a, b))
}

object Integral {
  def apply[A](implicit ev: Integral[A]): Integral[A] = ev

  trait IntegralOps[A] {
    def self: A
    def integralInstance: Integral[A]

    def abs: A = integralInstance.abs(self)
    def negate: A = integralInstance.negate(self)
    def signum: A = integralInstance.signum(self)
    def +(other: A): A = integralInstance.add(self, other)
    def -(other: A): A = integralInstance.sub(self, other)
    def *(other: A): A = integralInstance.mul(self, other)
    def /(other: A): A = integralInstance.div(self, other)
    def mod(other: A): A = integralInstance.mod(self, other)
    def divMod(other: A): (A, A) = integralInstance.divMod(self, other)
  }

  object ops {
    implicit def toIntegralOps[A: Integral](x: A): IntegralOps[A] = new IntegralOps[A] {
      override def self: A = x
      override def integralInstance: Integral[A] = implicitly[Integral[A]]
    }
  }

  implicit val short2Integral: Integral[Short] = new Integral[Short] {
    override def add(x: Short, y: Short): Short = toS(x + y)
    override def mul(x: Short, y: Short): Short = toS(x * y)
    override def div(a: Short, b: Short): Short = toS(a / b)
    override def mod(a: Short, b: Short): Short = {
      val res = a % b
      val m = if (res < 0) res + b else res
      toS(m)
    }

    override def negate(x: Short): Short = toS(-x)
    override def fromInteger(n: Int): Short = toS(n)
    override def signum(x: Short): Short = x match {
      case 0          => 0
      case _ if x < 0 => -1
      case _ if x > 0 => 1
    }

    override def show(x: Short): String = x.toString
    override def eq(lhs: Short, rhs: Short): Boolean = lhs equals rhs

    private def toS(n: Int): Short = n.toShort
  }

  implicit val int2Integral: Integral[Int] = new Integral[Int] {
    override def add(x: Int, y: Int): Int = x + y
    override def mul(x: Int, y: Int): Int = x * y
    override def div(a: Int, b: Int): Int = a / b
    override def mod(a: Int, b: Int): Int = {
      val res = a % b
      if (res < 0) res + b else res
    }

    override def negate(x: Int): Int = -x
    override def fromInteger(n: Int): Int = n
    override def signum(x: Int): Int = x match {
      case 0          => 0
      case _ if x < 0 => -1
      case _ if x > 0 => 1
    }

    override def show(x: Int): String = x.toString
    override def eq(lhs: Int, rhs: Int): Boolean = lhs equals rhs
  }

  implicit val long2Integral: Integral[Long] = new Integral[Long] {
    override def add(x: Long, y: Long): Long = x + y
    override def mul(x: Long, y: Long): Long = x * y
    override def div(a: Long, b: Long): Long = a / b
    override def mod(a: Long, b: Long): Long = {
      val res = a % b
      if (res < 0) res + b else res
    }

    override def negate(x: Long): Long = -x
    override def fromInteger(n: Int): Long = n.toLong
    override def signum(x: Long): Long = x match {
      case 0          => 0
      case _ if x < 0 => -1
      case _ if x > 0 => 1
    }

    override def show(x: Long): String = x.toString
    override def eq(lhs: Long, rhs: Long): Boolean = lhs equals rhs
  }
}