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

package io.hascalator.data

import io.hascalator.AbstractTestSpec
import Maybe._

class MaybeSpec extends AbstractTestSpec {
  describe("Maybe") {
    describe("map") {
      it("should apply a function to Some values") {
        just(21).map(_ * 2) shouldBe just(42)
        just(21).map(identity) shouldBe just(21)
      }

      it("should produce a none applying a function to none values") {
        none[Int].map(_ * 2) shouldBe none
      }
    }

    describe("flatMap") {
      it("should apply a function to Some values and flatten the result") {
        just(21).flatMap(x => just(x * 2)) shouldBe just(42)
      }

      it("should return back a none applying a function to none values") {
        none[Int].flatMap(x => just(x * 2)) shouldBe none
      }
    }

    describe("getOrElse") {
      it("should return the value wrapped in a Some") {
        just(42).getOrElse(-1) shouldBe 42
      }

      it("should return the default value for none") {
        none.getOrElse(-1) shouldBe -1
      }
    }

    describe("orElse") {
      it("should return the value wrapped in a Some") {
        just(42).orElse(just(-1)) shouldBe just(42)
      }

      it("should return the default value for none") {
        none.getOrElse(just(-1)) shouldBe just(-1)
      }
    }

    describe("filter") {
      it("should return the Some value if it matches the predicate") {
        just(42).filter(_ % 2 == 0) shouldBe just(42)
      }

      it("should return none if the Some value is not matching the predicate") {
        just(41).filter(_ % 2 == 0) shouldBe none
      }

      it("should return none if the none value is not matching the predicate") {
        none[Int].filter(_ % 2 == 0) shouldBe none
      }
    }
  }
}
