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
    describe("Eq[Set]") {
      it("should return true when comparing two empty sets") {
        Eq[Set[Int]].eq(emptySet, emptySet) shouldBe true
      }

      it("should return false comparing the empty set against a non empty set") {
        Eq[Set[Int]].eq(emptySet, set(1 to 10)) shouldBe false
        Eq[Set[Int]].eq(set(1 to 10), emptySet) shouldBe false
      }

      it("should return true comparing two sets with the same elements") {
        val range = 1 to 10
        val s1 = set(range)
        val s2 = set(range.reverse)

        Eq[Set[Int]].eq(s1, s2) shouldBe true
        Eq[Set[Int]].eq(s2, s1) shouldBe true
      }
    }

    describe("Show[Set]") {
      it("should produce a string representation for the empty set") {
        Show[Set[Int]].show(emptySet) shouldBe "fromList []"
      }

      it("should produce a string representation for non empty sets") {
        Show[Set[Int]].show(set(1 to 5)) shouldBe "fromList [1, 2, 3, 4, 5]"
        Show[Set[Int]].show(set((1 to 5).reverse)) shouldBe "fromList [1, 2, 3, 4, 5]"
      }
    }

    describe("size") {
      it("should return 0 for empty sets") {
        emptySet.size shouldBe 0
      }

      it("should return 1 for singleton sets") {
        singletonSet(42).size shouldBe 1
      }

      it("should return the number of elements in a non empty set") {
        set(1 to 10).size shouldBe 10
      }
    }

    describe("isEmpty") {
      it("should return true for empty sets") {
        emptySet.isEmpty shouldBe true
      }

      it("should return false for singleton sets") {
        singletonSet(42).isEmpty shouldBe false
      }

      it("should return false for non empty sets") {
        set(1 to 10).isEmpty shouldBe false
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

      it("should return true when the element is in a non empty set") {
        set(1 to 10).member(6) shouldBe true
      }

      it("should return false when the element is not in a non empty set") {
        set(1 to 10).member(16) shouldBe false
      }
    }

    describe("notMember") {
      it("should always return true for empty sets") {
        emptySet.notMember(5) shouldBe true
      }

      it("should return true when the element is not a member of a set") {
        set(1 to 10).notMember(17) shouldBe true
      }

      it("should return false when the element is a member of a set") {
        set(1 to 10).notMember(7) shouldBe false
      }
    }

    describe("toAscList") {
      it("should produce the empty list from empty sets") {
        emptySet.toAscList shouldBe List.empty
      }

      it("should return an ascendent list") {
        val r = 1 to 6
        set(r.reverse).toAscList shouldBe List.fromRange(r)
      }
    }

    describe("toDescList") {
      it("should produce the empty list from empty sets") {
        emptySet.toDescList shouldBe List.empty
      }

      it("should return an descendent list") {
        val r = 1 to 6
        set(r).toDescList shouldBe List.fromRange(r.reverse)
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

    describe("map") {
      it("should return the empty set mapping over the empty set") {
        emptySet.map(_ * 2) shouldBe emptySet
      }

      it("should map over a non empty set") {
        val f = (x: Int) => x * 2
        val r = 1 to 5

        set(r).map(f) shouldBe Set.fromList(List.fromRange(r).map(f))
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

    describe("isSubsetOf") {
      it("should return true for two empty sets") {
        emptySet.isSubsetOf(emptySet) shouldBe true
      }

      it("should return true for an empty set to be a subset of any non empty set") {
        emptySet.isSubsetOf(set(1 to 10)) shouldBe true
      }

      it("should return true for a set to be a subset of itself") {
        val s = set(1 to 10)
        s.isSubsetOf(s) shouldBe true
      }
    }

    describe("isProperSubsetOf") {
      it("should return false checking whether an empty set is a proper subset of another empty set") {
        emptySet.isProperSubsetOf(emptySet) shouldBe false
      }

      it("should return true when one set is a subset of the other, but they are not equals") {
        val s1 = set(1 to 9)
        val s2 = set(1 to 10)

        s1.isProperSubsetOf(s2) shouldBe true
      }

      it("should return false when one set is a subset of the other, but they are equals") {
        val s1 = set(1 to 9)
        val s2 = set((1 to 9).reverse)

        s1.isProperSubsetOf(s2) shouldBe false
      }
    }

    describe("splitRoot") {
      it("should return the empty list trying to splitRoot the empty set") {
        emptySet.splitRoot shouldBe List.empty
      }

      it("should split a set according to its tree representation") {
        val list: List[Set[Int]] = Set.fromList(List.fromRange(1 to 6)).splitRoot
        val s1 :: s2 :: s3 :: Nil = list
        s1 shouldBe set(1 to 3)
        s2 shouldBe singletonSet(4)
        s3 shouldBe set(5 to 6)
      }
    }

    describe("splitMember") {
      it("should return false and two empty sets from the empty set") {
        val expected = (emptySet, false, emptySet)
        emptySet.splitMember(4) shouldBe expected
      }

      it("should split a set when the member is found") {
        val (leftSet, found, rightSet) = set(1 to 10).splitMember(5)
        found shouldBe true
        leftSet shouldBe set(1 to 4)
        rightSet shouldBe set(6 to 10)
      }

      it("should not split a set when the member is not found") {
        val (leftSet, found, rightSet) = set(1 to 10).splitMember(15)
        found shouldBe false
        leftSet shouldBe set(1 to 10)
        rightSet shouldBe emptySet
      }
    }

    describe("intersection") {
      it("should produce the empty set when both sets are empty") {
        emptySet.intersection(emptySet) shouldBe emptySet
      }

      it("should produce the empty set as intersection when one set is empty") {
        var s = set(1 to 6)
        emptySet.intersection(s) shouldBe emptySet
        s.intersection(emptySet) shouldBe emptySet
      }

      it("should make the intersection of two non empty sets") {
        val s1 = set(1 to 6)
        val s2 = set((4 to 10).reverse)
        s1.intersection(s2) shouldBe s2.intersection(s1)
        s1.intersection(s2) shouldBe set(4 to 6)
      }
    }

    describe("union") {
      it("should produce the empty set when both sets are empty") {
        emptySet.union(emptySet) shouldBe emptySet
      }

      it("should produce the non empty set as union when the other set is empty") {
        var s = set(1 to 6)
        emptySet.union(s) shouldBe s
        s.union(emptySet) shouldBe s
      }

      it("should make the union of two non empty sets") {
        val s1 = set(1 to 6)
        val s2 = set((4 to 10).reverse)
        s1.union(s2) shouldBe s2.union(s1)
        s1.union(s2) shouldBe set(1 to 10)
      }
    }

    describe("difference") {
      it("should return the empty set making the difference of two empty sets") {
        emptySet.difference(emptySet) shouldBe emptySet
      }

      it("should return the non empty set when substracting the empty set") {
        val s = set(1 to 6)
        s.difference(emptySet) shouldBe s
        emptySet.difference(s) shouldBe emptySet
      }

      it("should make the difference of two non empty sets") {
        val s1 = set(1 to 10)
        val s2 = set(6 to 10)

        s1.difference(s2) shouldBe set(1 to 5)
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