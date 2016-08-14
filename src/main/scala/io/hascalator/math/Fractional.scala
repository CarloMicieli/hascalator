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
  * It represents the type class for fractional numbers, supporting real division.
  */
@implicitNotFound("The type ${A} was not made an instance of the Fractional type class")
trait Fractional[A] extends Num[A] {
  def div(x: A, y: A): A
  def recip(x: A): A = div(one, x)
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

  implicit val floatToFractional: Fractional[Float] = new Fractional[Float] {
    override def div(x: Float, y: Float): Float = x / y

    override def mul(x: Float, y: Float): Float = x * y

    override def negate(x: Float): Float = -x

    override def signum(x: Float): Float = x match {
      case 0.0f          => 0.0f
      case _ if x > 0.0f => 1.0f
      case _ if x < 0.0f => -1.0f
    }

    override def add(x: Float, y: Float): Float = x + y

    override val one: Float = 1.0f
    override val zero: Float = 0.0f
  }

  implicit val doubleToFractional: Fractional[Double] = new Fractional[Double] {
    override def div(x: Double, y: Double): Double = x / y

    override def mul(x: Double, y: Double): Double = x * y

    override def negate(x: Double): Double = -x

    override def signum(x: Double): Double = x match {
      case 0.0          => 0.0
      case _ if x > 0.0 => 1.0
      case _ if x < 0.0 => -1.0
    }

    override def add(x: Double, y: Double): Double = x + y

    override val zero: Double = 0.0
    override val one: Double = 1.0
  }
}