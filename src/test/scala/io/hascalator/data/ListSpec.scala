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

import io.hascalator.{ AbstractTestSpec, ApplicationException }
import io.hascalator.typeclasses.{ Eq, Ord, Ordering, Show }

class ListSpec extends AbstractTestSpec with SampleLists {
  describe("A list") {
    describe("Show") {
      it("should make an instance") {
        import Show.ops._
        List(1, 2, 23, 4).show shouldBe "[1, 2, 23, 4]"
      }
    }

    describe("Patter match") {
      it("should match the empty list") {
        val res = emptyList match {
          case List() => true
          case _      => false
        }
        res shouldBe true
      }

      it("should match the constructed list") {
        val res = List(1, 2, 3) match {
          case List(1, 2, 3) => true
          case _             => false
        }
        res shouldBe true
      }
    }

    describe("elem") {
      it("should return false for empty lists") {
        emptyList elem 42 shouldBe false
      }

      it("should return true if the list contains the element") {
        numbersList elem 6 shouldBe true
      }

      it("should return false if the list doesn't contain the element") {
        numbersList elem 999 shouldBe false
      }
    }

    describe("find") {
      it("should return a None for empty lists") {
        emptyList.find(_ == 42) shouldBe Maybe.none
      }

      it("should return a Just with the element which match the predicate") {
        numbersList.find(_ == 6) shouldBe Maybe.just(6)
      }

      it("should return a Just for the leftmost element which match the predicate") {
        numbersList.find(_ > 0) shouldBe Maybe.just(1)
      }

      it("should return a None if no element in the list is matching the predicate") {
        numbersList.find(_ == 999) shouldBe Maybe.none
      }
    }

    describe("foreach") {
      it("should apply a function to list elements, for its side-effect") {
        var res = 0
        numbersList.foreach(n => res = res + n)
        res shouldBe 55
      }

      it("should not apply the function for empty lists") {
        var applied = false
        emptyList.foreach(n => applied = true)
        applied shouldBe false
      }
    }

    describe("filter") {
      it("should return the empty list, filtering the empty list") {
        emptyList.filter(_ % 2 == 0) should be theSameInstanceAs emptyList
      }

      it("should filter out elements that don't match the filter predicate") {
        numbersList.filter(_ % 2 == 0) shouldBe List(2, 4, 6, 8, 10)
      }

      it("should produce the empty list, if no element matches the predicate") {
        numbersList.filter(_ > 999) shouldBe List()
      }

      it("should produce a copy of the original list when all elements match the predicate") {
        val list = numbersList.filter(_ > 0)
        list shouldBe numbersList
        list shouldNot be theSameInstanceAs numbersList
      }
    }

    describe("filterNot") {
      it("should return the empty list, filtering the empty list") {
        emptyList.filterNot(_ % 2 == 0) should be theSameInstanceAs emptyList
      }

      it("should filter out elements that match the filter predicate") {
        numbersList.filterNot(_ % 2 != 0) shouldBe List(2, 4, 6, 8, 10)
      }

      it("should produce the empty list, if all elements match the predicate") {
        numbersList.filterNot(_ < 999) shouldBe List()
      }

      it("should produce a copy of the original list when all elements match the predicate") {
        val list = numbersList.filterNot(_ < 0)
        list shouldBe numbersList
        list shouldNot be theSameInstanceAs numbersList
      }
    }

    describe("+:") {
      it("should add the new element in front of the list") {
        val list = 1 +: emptyList
        list.head shouldBe 1
      }

      it("should create a new list, with size increased by 1") {
        val list = 1 +: emptyList
        emptyList should be theSameInstanceAs emptyList
        list should have length 1
      }
    }

    describe("unCons") {
      it("should return a None for the empty list") {
        emptyList.unCons shouldBe None
      }

      it("should return a pair with head and tail for non empty lists") {
        numbersList.unCons shouldBe Just((numbersList.head, numbersList.tail))
      }
    }

    describe("take") {
      it("should return an empty list, take the first n elements from empty lists") {
        val l = emptyList take 5
        l.isEmpty shouldBe true
      }

      it("should take the first n elements from the list") {
        val l = numbersList take 5
        l shouldBe List(1, 2, 3, 4, 5)
      }
    }

    describe("takeWhile") {
      it("should remove the prefix until the predicate matches") {
        val l = randomList takeWhile { _ != 2 }
        l shouldBe List(56, 34)
      }

      it("should produce the empty list, from empty lists") {
        emptyList takeWhile { _ != 2 } shouldBe Nil
      }
    }

    describe("drop") {
      it("should remove the first n elements from the list") {
        val l2 = numbersList drop 5
        l2 shouldBe List(6, 7, 8, 9, 10)
      }

      it("should return the empty list, dropping n elements from the empty list") {
        val l1 = emptyList drop 5
        l1 shouldBe List()
      }
    }

    describe("dropWhile") {
      it("should remove the prefix until the predicate matches") {
        val l = randomList dropWhile { _ != 2 }
        l shouldBe List(2, 9, 15, 99, 52)
      }

      it("should produce an empty list, from empty lists") {
        emptyList.dropWhile(_ != 2) shouldBe Nil
      }
    }

    describe("++") {
      it("should produce a new list, concatenating the two lists elements") {
        val l1 = List(5, 6, 7, 8, 9, 10)
        val l2 = List(1, 2, 3, 4, 5)

        val l3 = l1 ++ l2
        l3 shouldBe List(5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5)
      }

      it("should return the original list, if the other is empty") {
        numbersList ++ List() should be theSameInstanceAs numbersList
        List() ++ numbersList should be theSameInstanceAs numbersList
      }

      it("++ and append are the same") {
        val l1 = List(5, 6, 7, 8, 9, 10)
        val l2 = List(1, 2, 3, 4, 5)

        l1 ++ l2 shouldBe (l1 append l2)
      }
    }

    describe("isEmpty") {
      it("should return true for empty lists") {
        emptyList.isEmpty shouldBe true
        emptyList.nonEmpty shouldBe false
      }

      it("should return false for non empty lists") {
        numbersList.isEmpty shouldBe false
        numbersList.nonEmpty shouldBe true
      }
    }

    describe("head") {
      import Maybe._
      it("should return None for empty list head") {
        emptyList.headMaybe shouldBe none
      }

      it("should return the element in front of this list") {
        numbersList.head shouldBe 1
        numbersList.headMaybe shouldBe just(1)
      }

      it("should throw an exception for empty lists head") {
        the[ApplicationException] thrownBy {
          emptyList.head
        } should have message "*** Exception: 'List.head: empty list'"
      }
    }

    describe("tail") {
      it("should throws an exception getting the list tail") {
        the[ApplicationException] thrownBy {
          emptyList.tail
        } should have message "*** Exception: 'List.tail: empty list'"
      }

      it("should return the tail for the list") {
        val list = numbersList.tail
        list.length shouldBe (numbersList.length - 1)
        list shouldBe List(2, 3, 4, 5, 6, 7, 8, 9, 10)
      }
    }

    describe("length") {
      it("should return the number of elements contained") {
        numbersList should have length 10
      }

      it("should return 0 for empty lists") {
        emptyList should have length 0
      }
    }

    describe("map") {
      it("should return an empty list, when the function is applied to empty lists") {
        val l = emptyList map { _ * 2 }
        l.isEmpty shouldBe true
      }

      it("should apply a function to list elements") {
        val l = numbersList.map { _ * 2 }
        l shouldBe List(2, 4, 6, 8, 10, 12, 14, 16, 18, 20)
      }
    }

    describe("flatMap") {
      it("should return an empty list, when the function is applied to empty lists") {
        val l = emptyList flatMap { x => List(2 * x) }
        l.isEmpty shouldBe true
      }

      it("should apply a function to list elements") {
        val l = numbersList flatMap { x => List(2 * x) }
        l shouldBe List(2, 4, 6, 8, 10, 12, 14, 16, 18, 20)
      }
    }

    describe("foldLeft") {
      it("should return the seed value for empty lists") {
        val result = emptyList.foldLeft(100)(_ - _)
        result shouldBe 100
      }

      it("should apply a binary operator to list elements, from left to right") {
        val result = numbersList.foldLeft(100)(_ - _)
        result shouldBe 45
      }
    }

    describe("foldRight") {
      it("should return the seed value for empty lists") {
        val result = emptyList.foldRight(100)(_ - _)
        result shouldBe 100
      }

      it("should apply a binary operator to list elements, from right to left") {
        val result = numbersList.foldRight(100)(_ - _)
        result shouldBe 95
      }
    }

    describe("reverse") {
      it("should return an empty list reversing an empty list") {
        emptyList.reverse should be theSameInstanceAs emptyList
      }

      it("should produce the list in reverse order") {
        val reversedList = numbersList.reverse
        reversedList.length === numbersList.length
        reversedList shouldBe List(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
      }
    }

    describe("splitAt") {
      it("should produce two empty lists, splitting an empty list") {
        emptyList.splitAt(1) should be((emptyList, emptyList))
      }

      it("should split the list at the given index") {
        numbersList.splitAt(4) shouldBe ((List(1, 2, 3, 4), List(5, 6, 7, 8, 9, 10)))
      }

      it("should produce an empty list as first element, if m is non positive") {
        numbersList.splitAt(-4) shouldBe ((Nil, numbersList))
      }

      it("should produce an empty list as second element, if m is greater than list length") {
        numbersList.splitAt(10) shouldBe ((numbersList, Nil))
      }
    }

    describe("partition") {
      it("should produce a pair of empty lists, when applied to empty lists") {
        emptyList.partition(_ > 100) shouldBe ((Nil, Nil))
      }

      it("should produce a pair of list applying a predicate") {
        numbersList.partition(_ % 2 == 0) shouldBe ((List(2, 4, 6, 8, 10), List(1, 3, 5, 7, 9)))
      }

      it("should produce a pair with a Nil as first element, if no element is matching the predicate") {
        numbersList.partition(_ > 999) shouldBe ((Nil, numbersList))
      }

      it("should produce a pair with a Nil as second element, if no element is matching the predicate") {
        numbersList.partition(_ < 999) shouldBe ((numbersList, Nil))
      }
    }

    describe("span") {
      it("should produce a pair of empty lists, when applied to empty lists") {
        emptyList.span(_ % 2 == 0) shouldBe ((Nil, Nil))
      }

      it("should produce a pair of lists") {
        numbersList.span(_ != 4) shouldBe ((List(1, 2, 3), List(4, 5, 6, 7, 8, 9, 10)))
        numbersList.span(_ == 4) shouldBe ((List(), List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)))
        numbersList.span(_ != 11) shouldBe ((List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), List()))
      }
    }

    describe("zip") {
      it("should produce the empty list if one the two lists is empty") {
        emptyList zip numbersList shouldBe List()
        numbersList zip emptyList shouldBe List()
      }

      it("should produce a number of elements as in the shortest list") {
        List('a', 'b', 'c') zip numbersList shouldBe List(('a', 1), ('b', 2), ('c', 3))
        numbersList zip List('a', 'b', 'c') shouldBe List((1, 'a'), (2, 'b'), (3, 'c'))
      }
    }

    describe("zipWith") {
      it("should produce an empty list if the original list is empty") {
        emptyList.zipWith(randomList)((a, b) => a) shouldBe List()
      }

      it("should create a new list applying the function to each corresponding pair") {
        numbersList.zipWith(numbersList)((a, b) => a + b) shouldBe List(2, 4, 6, 8, 10, 12, 14, 16, 18, 20)
      }
    }

    describe("equals") {
      it("should check whether two lists are equals") {
        val l1 = numbersList
        val l2 = numbersList
        val l3 = emptyList
        val l4 = emptyList

        l1 == l2 shouldBe true
        l1 == l3 shouldBe false
        l3 == l2 shouldBe false
        l3 == l4 shouldBe true
      }
    }

    describe("mkString") {
      it("should produce an empty string for empty lists") {
        emptyList.mkString(" - ") shouldBe ""
      }

      it("should produce a string with a given separator") {
        numbersList.mkString(";") shouldBe "1;2;3;4;5;6;7;8;9;10"
      }

      it("should produce a string with start and end strings") {
        numbersList.mkString(";", "<<", ">>") shouldBe "<<1;2;3;4;5;6;7;8;9;10>>"
      }
    }

    describe("toString") {
      it("should have '[]' as string representation for empty lists") {
        emptyList.toString shouldBe "[]"
      }

      it("should produce string representation for non empty lists") {
        numbersList.toString shouldBe "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]"
      }
    }

    describe("replicate") {
      it("should produce the empty string replicating the element 0 times") {
        List.replicate(0)('A') shouldBe emptyList
      }

      it("should produce a list with the same element") {
        List.replicate(5)('A') shouldBe List('A', 'A', 'A', 'A', 'A')
      }
    }

    describe("flatten") {
      it("should produce an empty list, flattening empty lists") {
        val empty: List[List[Int]] = Nil
        empty.flatten shouldBe empty
      }

      it("should produce a list, after the elements have been flatten") {
        val list = List(List(1, 2), List(3), List(4, 5))
        list.flatten shouldBe List(1, 2, 3, 4, 5)
      }
    }

    describe("all") {
      it("should return true for the empty list") {
        emptyList.all(_ > 100) shouldBe true
      }

      it("should return true if all elements match the predicate") {
        numbersList.all(_ > 0) shouldBe true
      }

      it("should return false if any element doesn't match the predicate") {
        numbersList.all(_ > 5) shouldBe false
      }
    }

    describe("any") {
      it("should return false for the empty list") {
        emptyList.any(_ > 100) shouldBe false
      }

      it("should return true if all elements match the predicate") {
        numbersList.any(_ > 0) shouldBe true
      }

      it("should return false if any element doesn't match the predicate") {
        numbersList.any(_ > 999) shouldBe false
      }
    }

    describe("unfoldRight") {
      it("should produce a new list from a seed value and a function") {
        val l = List.unfoldRight(1)(n => if (n < 10) Just((s"N($n)", n + 1)) else None)
        l shouldBe List("N(9)", "N(8)", "N(7)", "N(6)", "N(5)", "N(4)", "N(3)", "N(2)", "N(1)")
      }
    }

    describe("interperse") {
      it("should interperse a value between list elements") {
        val l = List('a', 'b', 'c', 'd', 'e').intersperse('-')
        l shouldBe List('a', '-', 'b', '-', 'c', '-', 'd', '-', 'e')
      }
    }

    describe("Eq[List]") {
      it("should be instance of the typeclass") {
        val eqInstance = implicitly[Eq[List[Int]]]
        eqInstance.eq(emptyList, emptyList) shouldBe true
        eqInstance.eq(emptyList, numbersList) shouldBe false
        eqInstance.eq(numbersList, emptyList) shouldBe false
        eqInstance.eq(numbersList, numbersList) shouldBe true
      }
    }

    describe("Ord[List]") {
      it("should be instance of the typeclass") {
        val ordInstance = implicitly[Ord[List[Int]]]
        ordInstance.compare(emptyList, emptyList) shouldBe Ordering.EQ
        ordInstance.compare(numbersList, numbersList) shouldBe Ordering.EQ
        ordInstance.compare(emptyList, numbersList) shouldBe Ordering.LT
        ordInstance.compare(numbersList, emptyList) shouldBe Ordering.GT
        ordInstance.compare(numbersList, randomList) shouldBe Ordering.LT
        ordInstance.compare(randomList, numbersList) shouldBe Ordering.GT
      }
    }
  }
}

trait SampleLists {
  val emptyList: List[Int] = List()
  val numbersList: List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  val randomList: List[Int] = List(56, 34, 2, 9, 15, 99, 52)
  def list[A](items: A*): List[A] = List(items: _*)
}