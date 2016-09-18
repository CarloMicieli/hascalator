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
import io.hascalator.data.Rational

import scala.annotation.implicitNotFound
import scala.language.implicitConversions

/** It represents the type class for fractional numbers, supporting real division.
  * @tparam A the instance type
  * @author Carlo Micieli
  * @since 0.0.1
  */
@implicitNotFound("The type ${A} was not made an instance of the Fractional type class")
trait Fractional[A] extends Any with Num[A] {
  /** Returns the fractional division
    * @param x
    * @param y
    * @return
    */
  def div(x: A, y: A): A

  /** Returns the reciprocal fraction
    * @param x
    * @return
    */
  def recip(x: A): A = div(fromInteger(1), x)

  /** Conversion from a `Rational` (that is `Ratio[Integer]`)
    * @param r
    * @return
    */
  def fromRational(r: Rational): A
}

object Fractional {
  def apply[A](implicit fr: Fractional[A]): Fractional[A] = fr

  trait FractionalOps[A] {
    def self: A
    def fractionalInstance: Fractional[A]

    def recip: A = fractionalInstance.recip(self)
    def abs: A = fractionalInstance.abs(self)
    def negate: A = fractionalInstance.negate(self)
    def signum: A = fractionalInstance.signum(self)
    def +(other: A): A = fractionalInstance.add(self, other)
    def -(other: A): A = fractionalInstance.sub(self, other)
    def *(other: A): A = fractionalInstance.mul(self, other)
    def /(other: A): A = fractionalInstance.div(self, other)
  }

  object ops {
    implicit def toFractionalOps[A](x: A)(implicit fr: Fractional[A]): FractionalOps[A] = new FractionalOps[A] {
      override def self: A = x
      override def fractionalInstance: Fractional[A] = fr
    }
  }

  implicit val float2Fractional: Fractional[Float] = new Fractional[Float] {
    override def show(x: Float): String = x.toString
    override def add(x: Float, y: Float): Float = x + y
    override def div(x: Float, y: Float): Float = x / y
    override def mul(x: Float, y: Float): Float = x * y
    override def negate(x: Float): Float = -x
    override def fromInteger(n: Integer): Float = n.toFloat
    override def signum(x: Float): Float = scala.math.signum(x)
    override def eq(lhs: Float, rhs: Float): Boolean = lhs equals rhs
    override def fromRational(r: Rational): Float = (r.numerator / r.denominator).toFloat
  }

  implicit val double2Fractional: Fractional[Double] = new Fractional[Double] {
    override def show(x: Double): String = x.toString
    override def add(x: Double, y: Double): Double = x + y
    override def div(x: Double, y: Double): Double = x / y
    override def mul(x: Double, y: Double): Double = x * y
    override def negate(x: Double): Double = -x
    override def fromInteger(n: Integer): Double = n.toDouble
    override def signum(x: Double): Double = scala.math.signum(x)
    override def eq(lhs: Double, rhs: Double): Boolean = lhs equals rhs
    override def fromRational(r: Rational): Double = (r.numerator / r.denominator).toDouble
  }
}