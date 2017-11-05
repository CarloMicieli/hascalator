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
import Tuple._

class TupleSpec extends AbstractTestSpec with SamplePairs {
  describe("Tuples") {
    describe("swap") {
      it("should swap a pair elements") {
        val expected = (twoNumbers._2, twoNumbers._1)
        swap(twoNumbers) shouldBe expected
      }

      it("should return the original pair when swapped twice") {
        swap(swap(twoNumbers)) shouldBe twoNumbers
      }
    }

    describe("fst") {
      it("should return the first element") {
        fst(twoNumbers) shouldBe 42
        fst(numberAndString) shouldBe "answer"
      }
    }

    describe("snd") {
      it("should return the second element") {
        snd(twoNumbers) shouldBe 1
        snd(numberAndString) shouldBe 42
      }
    }

    describe("curry") {
      it("should curry a function") {
        val f: ((Int, String)) => String = { case (n, s) => s"$s($n)" }
        curry(f)(42)("answer") shouldBe f((42, "answer"))
      }
    }

    describe("uncurry") {
      it("should uncurry a function") {
        val f: Int => String => String = n => s => s"$s($n)"
        uncurry(f)((42, "answer")) shouldBe f(42)("answer")
      }
    }

  }
}

trait SamplePairs {
  val twoNumbers = (42, 1)
  val numberAndString = ("answer", 42)
}