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

/** Extracting components of fractions.
  *
  * Minimal complete definition: [[properFraction]]
  *
  * @tparam A the instance type
  * @author Carlo Micieli
  * @since 0.0.1
  */
trait RealFrac[A] extends Real[A] with Fractional[A] {
  /** Takes a real fractional number `x` and returns a pair `(n, f)` such that `x = n + f`, and:
    *
    * - `n` is an integral number with the same sign as `x`; and
    * - `f` is a fraction with the same type and sign as `x`, and with absolute value less than 1.
    *
    * @param a
    * @tparam B
    * @return
    */
  def properFraction[B: Integral](a: A): (B, A)

  /** Returns the integer nearest x between zero and x
    * @param x
    * @tparam B
    * @return
    */
  def truncate[B: Integral](x: A): B = {
    val (m, _) = properFraction(x)
    m
  }

  /** Returns the nearest integer to x; the even integer if x is equidistant between two integers
    * @param x
    * @tparam B
    * @return
    */
  def round[B: Integral](x: A): B = {
    val zero = fromInteger(0)
    val one = Integral[B].fromInteger(1)
    val (n, r) = properFraction(x)
    val m = if (lt(r, zero)) {
      Integral[B].sub(n, one)
    } else {
      Integral[B].add(n, one)
    }

    val half: A = recip(fromInteger(1))

    val sign: A = signum(abs(sub(r, half)))
    if (eq(sign, zero)) {
      if (even(n)) n else m
    } else if (eq(sign, fromInteger(-1))) {
      n
    } else {
      m
    }
  }

  /** Returns the least integer not less than x
    * @param x
    * @tparam B
    * @return
    */
  def ceiling[B: Integral](x: A): B = {
    val (n, r) = properFraction(x)
    if (gt(r, fromInteger(0))) {
      Integral[B].add(n, Integral[B].fromInteger(1))
    } else {
      n
    }
  }

  /** Returns the greatest integer not greater than x
    * @param x
    * @tparam B
    * @return
    */
  def floor[B: Integral](x: A): B = {
    val (n, r) = properFraction(x)
    if (lt(r, fromInteger(0))) {
      Integral[B].sub(n, Integral[B].fromInteger(1))
    } else {
      n
    }
  }
}

object RealFrac {
  def apply[A](implicit r: RealFrac[A]): RealFrac[A] = r

  implicit val realFracDouble: RealFrac[Double] = new RealFrac[Double] {
    override def properFraction[B: Integral](a: Double): (B, Double) = undefined
    override def toRational(x: Double): Rational = undefined
    override def compare(x: Double, y: Double): Ordering = Ord[Double].compare(x, y)
    override def div(x: Double, y: Double): Double = x / y
    override def mul(x: Double, y: Double): Double = x * y
    override def negate(x: Double): Double = -x
    override def fromInteger(n: Integer): Double = n.toDouble
    override def signum(x: Double): Double = undefined
    override def add(x: Double, y: Double): Double = x + y
    override def eq(lhs: Double, rhs: Double): Boolean = Eq[Double].eq(lhs, rhs)
    override def show(x: Double): String = Show[Double].show(x)
    override def fromRational(r: Rational): Double = r.numerator.toDouble / r.denominator.toDouble
  }
}