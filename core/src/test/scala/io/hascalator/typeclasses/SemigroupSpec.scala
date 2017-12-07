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

import Prelude._
import io.hascalator.data.NonEmpty

class SemigroupSpec extends AbstractTestSpec {
  implicit val intSemigroup: Semigroup[Int] = new Semigroup[Int] {
    override def mappend(x: Int, y: Int): Int = x + y
  }

  describe("SemiGroup") {
    describe("mAppend") {
      it("should append two values") {
        Semigroup[Int].mappend(1, 2) shouldBe 3
      }
    }

    describe("sConcat") {
      it("should apply <> to NonEmpty lists") {
        Semigroup[Int].sConcat(NonEmpty(1, 2, 3, 4)) shouldBe 10
      }
    }

    describe("sTimes") {
      it("should apply <> n times") {
        Semigroup[Int].sTimes(4)(2) shouldBe 4 * 2
      }

      it("should throw an exception when times == 0") {
        the[ApplicationException] thrownBy {
          Semigroup[Int].sTimes(0)(2)
        } should have message "*** Exception: Semigroup.sTimes: invalid value"
      }
    }
  }
}
