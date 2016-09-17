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

class IntegralSpec extends AbstractTestSpec with SampleIntegralInstance {
  describe("Integral typeclass") {
    describe("quot") {
      it("should return the quot") {
        instance.quot(42, 7) shouldBe 6
        instance.quot(42, 8) shouldBe 5
      }

      it("should truncate torward zero") {
        instance.quot(-42, 5) shouldBe -8
      }
    }

    describe("rem") {
      it("should return the remainder") {
        instance.rem(42, 7) shouldBe 0
        instance.rem(43, 7) shouldBe 1
      }

      it("should truncate torward zero") {
        instance.rem(-42, 5) shouldBe -2
      }
    }

    describe("div") {
      it("should return the div") {
        instance.div(42, 7) shouldBe 6
        instance.div(42, 8) shouldBe 5
      }

      it("should truncate torward negative infinity") {
        instance.div(-42, 5) shouldBe -9
      }
    }

    describe("mod") {
      it("should return the remainder") {
        instance.mod(42, 7) shouldBe 0
        instance.mod(43, 7) shouldBe 1
      }

      it("should truncate torward negative infinity") {
        instance.mod(-42, 5) shouldBe 3
      }
    }

    describe("divMod") {
      it("should return simultaneously div and mod") {
        instance.divMod(43, 7) shouldBe ((6, 1))
      }
    }
  }
}

trait SampleIntegralInstance {
  val instance = Integral[Int]
}