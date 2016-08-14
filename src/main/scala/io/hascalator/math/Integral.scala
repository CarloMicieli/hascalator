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

package io.hascalator.math

import scala.annotation.implicitNotFound
import scala.language.implicitConversions

/**
  * It represents the type class for types that implement the numeric operation for
  * whole numbers.
  *
  * @tparam A the type class instance type
  */
@implicitNotFound("The type ${A} was not made an instance of the Integral type class")
trait Integral[A] extends Num[A] {

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
    def div(other: A): A = integralInstance.div(self, other)
    def mod(other: A): A = integralInstance.mod(self, other)
    def divMod(other: A): (A, A) = integralInstance.divMod(self, other)
  }

  object ops {
    implicit def toIntegralOps[A: Integral](x: A): IntegralOps[A] = new IntegralOps[A] {
      override def self: A = x
      override def integralInstance: Integral[A] = implicitly[Integral[A]]
    }
  }

  private type Fun[A] = (A, A) => A
  def apply[A](z: A, o: A)(fAdd: Fun[A], fMul: Fun[A], fDiv: Fun[A], fMod: Fun[A])(fNegate: A => A)(fSignum: A => A): Integral[A] = new Integral[A] {

    override val zero: A = z
    override val one: A = o

    override def div(a: A, b: A): A = fDiv(a, b)
    override def mod(a: A, b: A): A = fMod(a, b)
    override def add(x: A, y: A): A = fAdd(x, y)
    override def mul(x: A, y: A): A = fMul(x, y)
    override def negate(x: A): A = fNegate(x)
    override def signum(x: A): A = fSignum(x)
  }

  implicit val intToIntegral: Integral[Int] = {
    def signum(x: Int): Int = x match {
      case 0          => 0
      case _ if x < 0 => -1
      case _ if x > 0 => 1
    }

    def mod(x: Int, y: Int): Int = {
      val res = x % y
      if (res < 0) res + y else res
    }

    Integral(0, 1)(_ + _, _ * _, _ / _, mod)(x => -x)(signum)
  }

  implicit val longToIntegral: Integral[Long] = {
    def signum(x: Long): Long = x match {
      case 0          => 0
      case _ if x < 0 => -1
      case _ if x > 0 => 1
    }

    def mod(x: Long, y: Long): Long = {
      val res = x % y
      if (res < 0) res + y else res
    }

    Integral(0L, 1L)(_ + _, _ * _, _ / _, mod)(x => -x)(signum)
  }
}