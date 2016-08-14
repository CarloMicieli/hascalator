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

import scala.language.implicitConversions

/**
  * Rational numbers, with numerator and denominator of some Integral type.
  *
  * @param n the numerator
  * @param d the denominator
  * @tparam A
  */
class Ratio[A: Integral] protected (n: A, d: A) {
  private val I = implicitly[Integral[A]]
  private val g = gcd(n, d)

  def this(n: A) = this(n, implicitly[Integral[A]].fromInteger(1))

  /**
    * Extract the numerator of the ratio in reduced form: the numerator and denominator
    * have no common factor and the denominator is positive.
    */
  val numerator: A = I.div(n, g)

  /**
    * Extract the denominator of the ratio in reduced form: the numerator and denominator have no
    * common factor and the denominator is positive.
    */
  val denominator: A = I.div(d, g)

  def +(that: Ratio[A]): Ratio[A] = compute(that) {
    (a, b, c, d) =>
      new Ratio(I.add(I.mul(a, d), I.mul(b, c)), I.mul(b, d))
  }

  def -(that: Ratio[A]): Ratio[A] = compute(that) {
    (a, b, c, d) =>
      new Ratio(I.sub(I.mul(a, d), I.mul(b, c)), I.mul(b, d))
  }

  def *(that: Ratio[A]): Ratio[A] = compute(that) {
    (a, b, c, d) =>
      new Ratio(I.mul(a, c), I.mul(b, d))
  }

  def /(that: Ratio[A]): Ratio[A] = compute(that) {
    (a, b, c, d) =>
      new Ratio(I.mul(a, d), I.mul(b, c))
  }

  override def toString: String = {
    if (d == I.fromInteger(1)) {
      n.toString
    } else {
      s"$n/$d"
    }
  }

  override def equals(o: Any): Boolean = {
    o match {
      case that: Ratio[_] =>
        this.numerator == that.numerator && this.denominator == that.denominator
      case _ =>
        false
    }
  }

  private def compute(that: Ratio[A])(f: (A, A, A, A) => Ratio[A]): Ratio[A] = {
    Ratio.compute(this, that)(f)
  }

  @annotation.tailrec
  private def gcd(a: A, b: A): A = {
    if (b == I.fromInteger(0)) {
      a
    } else {
      gcd(b, I.mod(a, b))
    }
  }
}

object Ratio {
  def apply[A](n: A)(implicit I: Integral[A]): Ratio[A] = {
    apply(n, I.fromInteger(1))
  }

  def apply[A](n: A, d: A)(implicit I: Integral[A]): Ratio[A] = {
    require(d != I.fromInteger(0))
    if (n == d) {
      val one = I.fromInteger(1)
      new Ratio(one, one)
    } else {
      new Ratio(n, d)
    }
  }

  implicit def integral2Ratio[A: Integral](x: A): Ratio[A] = {
    Ratio[A](x)
  }

  private def compute[A: Integral](r1: Ratio[A], r2: Ratio[A])(f: (A, A, A, A) => Ratio[A]): Ratio[A] = {
    f(r1.numerator, r1.denominator, r2.numerator, r2.denominator)
  }
}