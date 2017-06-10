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

/** @author Carlo Micieli
  */
class FractionSpec extends AbstractTestSpec with SampleFractions {
  describe("A Fraction") {
    describe("apply") {
      it("should create Fraction values") {
        val oneHalf = Fraction(1, 2)
        oneHalf.numerator shouldBe 1
        oneHalf.denominator shouldBe 2
      }

      it("should create Fractions from whole numbers") {
        val two = Fraction(2)
        two.numerator shouldBe 2
        two.denominator shouldBe 1
      }

      it("should create Fractions from negative numbers") {
        val f1 = Fraction(-1, 2)
        val f2 = Fraction(1, -2)
        val f3 = Fraction(-1, -2)

        f1 shouldEqual f2
        f3 shouldEqual f1
        f2 shouldEqual f2
      }

      it("should always produce fractions in their lowest terms") {
        val f = Fraction(6, 63)
        f.toString() shouldBe "2/21"
      }

      it("should throw an exception when denominator is 0") {
        the[IllegalArgumentException] thrownBy {
          Fraction(42, 0)
        } should have message "requirement failed: denominator must be != 0"
      }
    }

    describe("equals") {
      it("should check whether two fractions are equals") {
        (Fraction(1, 2) == Fraction(1, 2)) shouldBe true
        (Fraction(1, 2) == Fraction(5, 2)) shouldBe false
        (Fraction(5, 2) == Fraction(1, 2)) shouldBe false
      }
    }

    describe("add") {
      it("should sum two fractions") {
        (oneHalf + twoThird) shouldBe Fraction(7, 6)
        (oneHalf plus twoThird) shouldBe Fraction(7, 6)
      }
    }

    describe("minus") {
      it("should sum two fractions") {
        (oneHalf - twoThird) shouldBe Fraction(-1, 6)
        (oneHalf minus twoThird) shouldBe Fraction(-1, 6)
      }
    }

    describe("times") {
      it("should multiply two fractions") {
        (twoThird * oneHalf) shouldBe Fraction(1, 3)
        (twoThird times oneHalf) shouldBe Fraction(1, 3)
      }
    }

    describe("div") {
      it("should multiply two fractions") {
        (twoThird / oneHalf) shouldBe Fraction(4, 3)
        (twoThird div oneHalf) shouldBe Fraction(4, 3)
      }

      it("should throw an exception when the numerator for the second Fraction is 0") {
        the[IllegalArgumentException] thrownBy {
          twoThird / Fraction(0, 5)
        } should have message "requirement failed: numerator for the second fraction must be != 0"
      }
    }

    describe("lessThan") {
      it("should compare two fractions") {
        (twoThird < oneHalf) shouldBe false
        (oneHalf < twoThird) shouldBe true
      }
    }

    describe("max") {
      it("should find the maximum fraction") {
        (twoThird max oneHalf) shouldBe twoThird
        (oneHalf max twoThird) shouldBe twoThird
      }
    }

    describe("min") {
      it("should find the minimum fraction") {
        (twoThird min oneHalf) shouldBe oneHalf
        (oneHalf min twoThird) shouldBe oneHalf
      }
    }

    describe("toString") {
      it("should produce String representations from Fractions") {
        oneHalf.toString shouldBe "1/2"
      }
    }
  }
}

trait SampleFractions {
  val twoThird = Fraction(2, 3)
  val oneHalf = Fraction(1, 2)
  val four = Fraction(4)
}