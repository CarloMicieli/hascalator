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
package math

import scala.inline
import scala.{ Option, Some }
import Prelude._

import scala.math.abs

/** A number that can be represented in the form a/b, where a and b are
  * both Int and b is not zero.
  *
  * @param num the numerator
  * @param den the denominator (must be != 0)
  *
  * @author Carlo Micieli
  * @since 0.0.1
  */
final class Fraction(num: Int, den: Int) {
  require(den != 0, "denominator must be != 0")

  private val g = gcd(abs(num), abs(den))

  /** This fraction numerator
    */
  val numerator: Int = abs(num / g) * sign(num, den)

  /** This fraction denominator
    */
  val denominator: Int = abs(den / g)

  /** A new Fraction for a whole number (ie, denominator is always 1).
    * @param n the numerator
    * @return a new Fraction
    */
  def this(n: Int) = this(n, 1)

  def plus(that: Fraction): Fraction = {
    val Fraction(a, b) = this
    val Fraction(c, d) = that
    Fraction(a * d + c * b, b * d)
  }

  @inline def +(that: Fraction): Fraction = this plus that

  def +(number: Int): Fraction = {
    new Fraction(num + number * den, den)
  }

  def minus(that: Fraction): Fraction = {
    val Fraction(a, b) = this
    val Fraction(c, d) = that
    new Fraction(a * d - c * b, b * d)
  }

  @inline def -(that: Fraction): Fraction = this minus that

  def times(that: Fraction): Fraction = {
    val Fraction(a, b) = this
    val Fraction(c, d) = that
    Fraction(a * c, b * d)
  }

  @inline def *(that: Fraction): Fraction = this times that

  def div(that: Fraction): Fraction = {
    require(that.numerator != 0, "numerator for the second fraction must be != 0")
    val Fraction(a, b) = this
    val Fraction(c, d) = that
    Fraction(a * d, b * c)
  }

  @inline def /(that: Fraction): Fraction = this div that

  def lessThan(that: Fraction): Boolean = {
    num * that.denominator < that.numerator * den
  }

  def <(that: Fraction): Boolean = {
    this lessThan that
  }

  def <=(that: Fraction): Boolean = {
    if (this == that) true else this lessThan that
  }

  def max(that: Fraction): Fraction = {
    if (this < that) that else this
  }

  def min(that: Fraction): Fraction = {
    if (this < that) this else that
  }

  override def equals(obj: Any): Boolean = {
    obj match {
      case that: Fraction => areEquals(this, that)
      case _              => false
    }
  }

  override def hashCode(): Int = numerator * 37 + denominator

  override def toString: String = s"$numerator/$denominator"

  // Greatest common divisor (https://en.wikipedia.org/wiki/Euclidean_algorithm)
  @tailrec private def gcd(x: Int, y: Int): Int = {
    if (y == 0) {
      x
    } else {
      gcd(y, x % y)
    }
  }

  private def areEquals(f1: Fraction, f2: Fraction) =
    f1.numerator == f2.numerator && f1.denominator == f2.denominator

  private def sign(n: Int, d: Int) = if (n < 0 || d < 0) -1 else 1
}

object Fraction {
  def apply(n: Int): Fraction = new Fraction(n)

  def apply(n: Int, d: Int): Fraction = new Fraction(n, d)

  def unapply(arg: Fraction): Option[(Int, Int)] = {
    Some((arg.numerator, arg.denominator))
  }
}
