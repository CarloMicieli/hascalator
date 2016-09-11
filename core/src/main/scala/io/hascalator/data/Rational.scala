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

/** Arbitrary-precision rational numbers, represented as a ratio of two `Int` values.
  * A rational number may be constructed using the `%%` operator.
  *
  * @param n the numerator
  * @param d the denominator
  * @author Carlo Micieli
  * @since 0.0.1
  */
final class Rational private (n: Int, d: Int) extends Ratio[Int](n, d) {
  override def equals(o: Any): Boolean = {
    o match {
      case that: Rational =>
        this.denominator == that.denominator && this.numerator == that.numerator
      case _ => false
    }
  }

  override def hashCode(): Int = {
    //TODO: to be checked
    17 * (37 * d.##) * (37 * n.##)
  }
}

object Rational {
  def apply(n: Int): Rational = {
    new Rational(n, 1)
  }

  def apply(n: Int, d: Int): Rational = {
    new Rational(n, d)
  }

  implicit class int2Rational(val n: Int) extends AnyVal {
    def %%(d: Int): Rational = Rational(n, d)
  }
}