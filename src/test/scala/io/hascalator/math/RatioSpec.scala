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

import io.hascalator.AbstractTestSpec

class RatioSpec extends AbstractTestSpec with RatioValues {
  describe("Ratio") {
    describe("constructor") {
      it("should construct int numbers") {
        val two = Ratio[Int](2)
        two.denominator shouldBe 1
        two.numerator shouldBe 2
      }

      it("should construct numbers") {
        val twoThirds = Ratio[Int](2, 3)
        twoThirds.numerator shouldBe 2
        twoThirds.denominator shouldBe 3
      }

      it("should reduce numerator/denominator") {
        val twoThirds = Ratio[Int](2 * 4, 3 * 4)
        twoThirds.numerator shouldBe 2
        twoThirds.denominator shouldBe 3
      }
    }

    describe("operations") {
      it("should sum two numbers") {
        (twoThirds + fourFifths) shouldBe Ratio[Int](22, 15)
      }

      it("should substract two numbers") {
        (twoThirds - fourFifths) shouldBe Ratio[Int](-2, 15)
      }

      it("should multiply two numbers") {
        (twoThirds * fourFifths) shouldBe Ratio[Int](8, 15)
      }

      it("should divide two numbers") {
        (twoThirds / fourFifths) shouldBe Ratio[Int](10, 12)
      }
    }
  }
}

trait RatioValues {
  val twoThirds = Ratio[Int](2, 3)
  val fourFifths = Ratio[Int](4, 5)
}

