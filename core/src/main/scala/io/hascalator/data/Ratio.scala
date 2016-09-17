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
package data

import Prelude._
import Integral.ops._

import scala.language.implicitConversions

/** Rational numbers, with numerator and denominator of some `Integral` type.
  *
  * @param n the numerator
  * @param d the denominator
  * @tparam A
  * @author Carlo Micieli
  * @since 0.0.1
  */
class Ratio[A: Integral] protected (n: A, d: A) {
  private val g = gcd(Integral[A].abs(n), Integral[A].abs(d))
  protected def this(n: A) = this(n, Integral[A].fromInteger(1))

  /** Extract the numerator of the ratio in reduced form: the numerator and denominator
    * have no common factor and the denominator is positive.
    */
  val numerator: A = {
    val I = Integral[A]
    val minus1 = I.fromInteger(-1)
    val num = Integral[A].div(n, g)

    if (I.eq(I.signum(d), minus1) && I.neq(I.signum(n), minus1)) {
      I.mul(minus1, num)
    } else {
      num
    }
  }

  /** Extract the denominator of the ratio in reduced form: the numerator and denominator have no
    * common factor and the denominator is positive.
    */
  val denominator: A = {
    Integral[A].abs(Integral[A].div(d, g))
  }

  /** Returns the sum of `this` and `that`.
    * @param that the second `Ratio` number
    * @return the sum
    */
  def +(that: Ratio[A]): Ratio[A] = compute(that) {
    (a, b, c, d) =>
      new Ratio((a * d) + (b * c), b * d)
  }

  /** Returns the difference of `this` and `that`.
    * @param that the second `Ratio` number
    * @return the difference
    */
  def -(that: Ratio[A]): Ratio[A] = compute(that) {
    (a, b, c, d) =>
      new Ratio((a * d) - (b * c), b * d)
  }

  /** Returns the product of `this` and `that`.
    * @param that the second `Ratio` number
    * @return the product
    */
  def *(that: Ratio[A]): Ratio[A] = compute(that) {
    (a, b, c, d) =>
      new Ratio(a * c, b * d)
  }

  /** Returns the division of `this` and `that`.
    * @param that the second `Ratio` number
    * @return the division
    */
  def /(that: Ratio[A]): Ratio[A] = compute(that) {
    (a, b, c, d) =>
      new Ratio(a * d, b * c)
  }

  override def toString: String = {
    Show[Ratio[A]].show(this)
  }

  override def equals(o: Any): Boolean = {
    o match {
      case that: Ratio[_] =>
        this.numerator == that.numerator && this.denominator == that.denominator
      case _ =>
        false
    }
  }

  override def hashCode(): Int = {
    //TODO: to be implemented
    super.hashCode()
  }

  private def compute(that: Ratio[A])(f: (A, A, A, A) => Ratio[A]): Ratio[A] = {
    Ratio.compute(this, that)(f)
  }

  @tailrec
  private def gcd(a: A, b: A): A = {
    if (b == Integral[A].fromInteger(0)) {
      a
    } else {
      gcd(b, Integral[A].mod(a, b))
    }
  }
}

object Ratio extends RatioInstances {
  /** Creates a new `Ratio` number with denominator equal to 1
    * @param n the numerator
    * @param I
    * @tparam A
    * @return a `Ratio` number
    */
  def apply[A](n: A)(implicit I: Integral[A]): Ratio[A] = {
    apply(n, I.fromInteger(1))
  }

  /** Creates a new `Ratio` number
    * @param n the numerator
    * @param d the denominator
    * @param I
    * @tparam A
    * @return a `Ratio` number
    */
  def apply[A](n: A, d: A)(implicit I: Integral[A]): Ratio[A] = {
    require(d != I.fromInteger(0))
    new Ratio(n, d)
  }

  implicit def integral2Ratio[A: Integral](x: A): Ratio[A] = {
    Ratio[A](x)
  }

  private def compute[A: Integral](r1: Ratio[A], r2: Ratio[A])(f: (A, A, A, A) => Ratio[A]): Ratio[A] = {
    f(r1.numerator, r1.denominator, r2.numerator, r2.denominator)
  }
}

trait RatioInstances {
  implicit def toShowRatio[A](implicit s: Show[A], i: Integral[A]): Show[Ratio[A]] = Show {
    (r: Ratio[A]) =>
      {
        val n = s.show(r.numerator)
        val d = s.show(r.denominator)
        if (r.denominator == Integral[A].fromInteger(1)) {
          n
        } else {
          s"$n/$d"
        }
      }
  }
}