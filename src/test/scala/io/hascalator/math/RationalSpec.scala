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
package math

import io.hascalator.AbstractTestSpec

class RationalSpec extends AbstractTestSpec with RationalNumbers {
  describe("A Rational number") {
    describe("numerator/denominator") {
      it("should have a numerator/denominator") {
        val twoThirds = Rational(2, 3)
        twoThirds.numerator shouldBe 2
        twoThirds.denominator shouldBe 3
      }

      it("should reduce the values") {
        val twoThirds = Rational(2 * 4, 3 * 4)
        twoThirds.numerator shouldBe 2
        twoThirds.denominator shouldBe 3
      }

      it("should create number from integer") {
        val five = Rational(5)
        five.numerator shouldBe 5
        five.denominator shouldBe 1
      }
    }

    describe("equals") {
      it("should return false for two different numbers") {
        (twoThirds equals fourFifths) shouldBe false
      }

      it("should return true for the same number") {
        (twoThirds equals Rational(2, 3)) shouldBe true
      }
    }
  }
}

trait RationalNumbers {
  val twoThirds = Rational(2, 3)
  val fourFifths = Rational(4, 5)
  val nineThirds = Rational(9, 3)
}