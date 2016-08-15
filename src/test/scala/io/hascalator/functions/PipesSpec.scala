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

package io.hascalator.functions

import io.hascalator.AbstractTestSpec

class PipesSpec extends AbstractTestSpec with SampleFunctions {
  describe("pipe-forward") {
    it("should pass intermediate values forward") {
      20 |> addOne |> double shouldBe 42
    }
  }

  describe("forward compose") {
    it("should chains functions") {
      val f = square >> triple
      f(3) shouldBe 27
    }
  }
}

trait SampleFunctions {
  val triple: Int => Int = _ * 3
  val square: Int => Int = x => x * x
  val addOne: Int => Int = _ + 1
  val double: Int => Int = _ * 2
}