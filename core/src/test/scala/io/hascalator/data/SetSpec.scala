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

    describe("foldLeft") {
      it("should return the initial value for empty sets") {
        emptySet.foldLeft(999)(_ + _) shouldBe 999
      }

      it("should apply the function to all set elements") {
        val list = List.fromRange(1 to 100)
        val set = Set.fromList(list)

        list.foldLeft(0)(_ + _) shouldBe set.foldLeft(0)(_ + _)
      }
    }

    describe("foldRight") {
      it("should return the initial value for empty sets") {
        emptySet.foldRight(999)(_ + _) shouldBe 999
      }

      it("should apply the function to all set elements") {
        val list = List.fromRange(1 to 100)
        val set = Set.fromList(list)

        list.foldRight(0)(_ + _) shouldBe set.foldRight(0)(_ + _)
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

    describe("lookupGE") {
      it("should find largest element smaller or equal to the given one") {
        val set = Set.fromList(List(3, 5))
        set.lookupGE(3) shouldBe Maybe.just(3)
        set.lookupGE(4) shouldBe Maybe.just(5)
        set.lookupGE(6) shouldBe Maybe.none
      }
    }

    describe("lookupLE") {
      it("should largest element smaller or equal to the given one") {
        val set = Set.fromList(List(3, 5))
        set.lookupLE(2) shouldBe Maybe.none
        set.lookupLE(4) shouldBe Maybe.just(3)
        set.lookupLE(5) shouldBe Maybe.just(5)
      }
    }

    describe("lookupLT") {
      it("should largest element smaller than the given one") {
        val set = Set.fromList(List(3, 5))
        set.lookupLT(3) shouldBe Maybe.none
        set.lookupLT(4) shouldBe Maybe.just(3)
      }
    }

    describe("lookupGT") {
      it("should largest element smaller than the given one") {
        val set = Set.fromList(List(3, 5))
        set.lookupGT(4) shouldBe Maybe.just(5)
        set.lookupGT(5) shouldBe Maybe.none
      }
    }

    describe("lookupMin") {
      it("should find the min value in a non empty set") {
        numbersSet.lookupMin shouldBe Maybe.just(1)
      }

      it("should return a None for the empty set") {
        emptySet.lookupMin shouldBe Maybe.none
      }
    }

    describe("findMin") {
      it("should find the min value in a non empty set") {
        numbersSet.findMin shouldBe 1
      }

      it("should throw an exception for empty sets") {
        the[ApplicationException] thrownBy {
          emptySet.findMin
        } should have message "*** Exception: Set.findMin: empty set has no minimal element"
      }
    }

    describe("lookupMax") {
      it("should find the max value in a non empty set") {
        numbersSet.lookupMax shouldBe Maybe.just(1000)
      }

      it("should return a None for the empty set") {
        emptySet.lookupMax shouldBe Maybe.none
      }
    }

    describe("findMax") {
      it("should find the max value in a non empty set") {
        numbersSet.findMax shouldBe 1000
      }

      it("should throw an exception for empty sets") {
        the[ApplicationException] thrownBy {
          emptySet.findMax
        } should have message "*** Exception: Set.findMax: empty set has no maximal element"
      }
    }

    describe("splitRoot") {
      it("split an empty set") {
        val ls: List[Set[Int]] = emptySet.splitRoot
        ls.isEmpty shouldBe true
      }

      it("split a non empty set") {
        val s = Set.fromList(List.fromRange(1 to 6))
        val (s1 :: s2 :: s3 :: Nil) = s.splitRoot
        s1.toString shouldBe "fromList [1]"
        s2.toString shouldBe "fromList [2]"
        s3.toString shouldBe "fromList [3, 4, 5, 6]"
      }
    }

    describe("delete") {
      it("should return the empty set deleting an element from an already empty set") {
        emptySet.delete(42).isEmpty shouldBe true
      }

      it("should return the empty set deleting the only element from a singleton set") {
        singletonSet(42).delete(42).isEmpty shouldBe true
      }

      it("should leave the set unchanged when the element to remove is not found") {
        val set = Set.fromList(List.fromRange(1 to 6))
        set.delete(42).toString shouldBe set.toString
      }

      it("should remove the element from a set") {
        val set = Set.fromList(List.fromRange(1 to 6))
        set.delete(2).member(2) shouldBe false
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

  def set(r: Range): Set[Int] = {
    r.foldLeft(Set.empty[Int])((set, x) => set.insert(x))
  }

  val numbersSet: Set[Int] = set(1 to 1000)
}