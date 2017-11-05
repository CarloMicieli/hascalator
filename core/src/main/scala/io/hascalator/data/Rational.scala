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
package data

import Prelude._

/** Arbitrary-precision rational numbers, represented as a ratio of two `Integer` values.
  * A rational number may be constructed using the `%%` operator.
  * @author Carlo Micieli
  * @since 0.0.1
  */
final class Rational(n: Integer, d: Integer) extends Ratio[Integer](n, d)

object Rational {
  def apply(n: Integer): Rational = {
    new Rational(n, 1)
  }

  def apply(n: Integer, d: Integer): Rational = {
    new Rational(n, d)
  }

  implicit class int2Rational(val n: Integer) extends AnyVal {
    def %%(d: Integer): Rational = apply(n, d)
  }
}
