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

class SetSpec extends AbstractTestSpec with SetsFixture {
  describe("A Set") {
    describe("size") {
      it("should return 0 for empty sets") {
        emptySet.size shouldBe 0
      }

      it("should return 1 for singleton sets") {
        singletonSet(42).size shouldBe 1
      }
    }

    describe("isEmpty") {
      it("should return true for empty sets") {
        emptySet.isEmpty shouldBe true
      }

      it("should return false for singleton sets") {
        singletonSet(42).isEmpty shouldBe false
      }
    }

    describe("member") {
      it("should return false for empty sets") {
        emptySet.member(3) shouldBe false
      }

      it("should return false for singleton sets when the element is not a member") {
        singletonSet(42).member(3) shouldBe false
      }

      it("should return true for singleton sets when the element is a member") {
        singletonSet(42).member(42) shouldBe true
      }
    }

    describe("insert") {
      it("should increase the set size by 1") {
        emptySet.insert(42).size shouldBe 1
        singletonSet(3).insert(42).size shouldBe 2
      }

      it("should rebalance the set") {
        val set = emptySet.insert(1).insert(2).insert(3).insert(4).insert(5).insert(6).insert(7)
        set.size shouldBe 7
        set.member(4) shouldBe true
        set.member(9) shouldBe false
      }
    }

    describe("fromList") {
      it("should create the empty set from the empty list") {
        Set.fromList(List.empty[Int]).isEmpty shouldBe true
      }

      it("should create a set with the list elements") {
        val s = Set.fromList(List(3, 1, 2))
        s.size shouldBe 3
        s.toAscList shouldBe List(1, 2, 3)
      }
    }
  }
}

trait SetsFixture {
  def emptySet: Set[Int] = Set.empty[Int]

  def singletonSet(x: Int): Set[Int] = Set.singleton(x)
}