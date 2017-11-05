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
import Either._

class EitherSpec extends AbstractTestSpec with EitherValues {
  describe("Either") {
    describe("pattern matching") {
      it("should be possible to match the left values") {
        val res = leftOne match {
          case Left(v)  => v
          case Right(_) => "not-found"
        }
        res shouldBe "one"
      }

      it("should be possible to match the right values") {
        val res = right42 match {
          case Left(_)  => -1
          case Right(v) => v
        }
        res shouldBe 42
      }
    }

    describe("isLeft") {
      it("should return false for right values") {
        right42.isLeft shouldBe false
      }

      it("should return true for left values") {
        leftOne.isLeft shouldBe true
      }
    }

    describe("isRight") {
      it("should return true for right values") {
        right42.isRight shouldBe true
      }

      it("should return false for left values") {
        leftOne.isRight shouldBe false
      }
    }

    describe("get") {
      it("should return the value wrapped in a right") {
        right42.get shouldBe 42
      }

      it("should throw an exception when the value is a left") {
        the[ApplicationException] thrownBy {
          leftOne.get
        } should have message "*** Exception: Left.get: this value is a Left"
      }
    }

    describe("getOrElse") {
      it("should return the value wrapped in a right") {
        right42.getOrElse(-1) shouldBe 42
      }

      it("should return the default when the value is a left") {
        leftOne.getOrElse(-1) shouldBe -1
      }
    }

    describe("foreach") {
      it("should apply the side effect function to right values") {
        var res = 0
        right42.foreach(x => res = res + 1)
        res shouldBe 1
      }

      it("should not run the side effect function for left values") {
        var res = 0
        leftOne.foreach(x => res = res + 1)
        res shouldBe 0
      }
    }

    describe("map") {
      it("should return the left value without any change") {
        val result = leftOne.map(_ + 1)
        result shouldBe leftOne
        result shouldBe theSameInstanceAs(leftOne)
      }

      it("should apply the function to the wrapped value in the right value") {
        right42.map(_ + 2) shouldBe right[String, Int](44)
      }
    }

    describe("flatMap") {
      it("should apply the function to the contained value in right values") {
        right42.flatMap(s => right(s * 2)) shouldBe right(84)
      }

      it("should return the same bad value, when applying the function to left values") {
        leftOne.flatMap(s => right(s * 2)) should be theSameInstanceAs leftOne
      }
    }

    describe("either") {
      it("should apply the left function to Left values") {
        leftOne.either(l => l.length)(r => r * 2) shouldBe 3
      }

      it("should apply the right function to Right values") {
        right42.either(l => l.length)(r => r * 2) shouldBe 84
      }
    }

    describe("zip") {
      it("should combine two Right values to a Right") {
        (right42 zip right42) shouldBe right((42, 42))
      }

      it("should combine Right and Left values to a Left") {
        (right42 zip leftOne) shouldBe left(List("one"))
        (leftOne zip right42) shouldBe left(List("one"))
      }

      it("should combine two Left values in a Left") {
        (leftOne zip leftOne) shouldBe left(List("one", "one"))
      }
    }

    describe("exists") {
      it("should apply a predicate to Right values") {
        right42 exists { _ > 0 } shouldBe true
      }

      it("should apply a predicate to Left values") {
        leftOne exists { _ > 0 } shouldBe false
      }
    }

    describe("swap") {
      it("should exchange Right with Left") {
        right42.swap shouldBe left(42)
      }

      it("should exchange Left with Right") {
        leftOne.swap shouldBe right("one")
      }
    }

    describe("toMaybe") {
      it("should convert a Right to a Just value") {
        right42.toMaybe shouldBe Maybe.just(42)
      }

      it("should convert a Left to a None value") {
        leftOne.toMaybe shouldBe Maybe.none[String]
      }
    }

    describe("cond") {
      it("should produce a right when the predicate is true") {
        Either.cond(42 == 42)("left", 1) shouldBe Either.right(1)
      }

      it("should produce a left when the predicate is false") {
        Either.cond(42 != 42)("left", 1) shouldBe Either.left("left")
      }
    }

    describe("toString") {
      it("should produce a string representation for Right values") {
        right42.toString shouldBe "Right(42)"
      }

      it("should produce a string representation for Left values") {
        leftOne.toString shouldBe "Left(one)"
      }
    }

    describe("lefts") {
      it("should return the empty list for list of only right values") {
        val res = Either.lefts[String, Int](List(right(1), right(2)))
        res shouldBe List.empty[String]
      }

      it("should return a list with only the left values") {
        val res = Either.lefts[String, Int](List(right(1), left("one"), left("two"), right(2)))
        res shouldBe List("one", "two")
      }
    }

    describe("rights") {
      it("should return the empty list for list of only left values") {
        val res = Either.rights[String, Int](List(left("one"), left("two")))
        res shouldBe List.empty[Int]
      }

      it("should return a list with only the right values") {
        val res = Either.rights[String, Int](List(right(1), left("one"), left("two"), right(2)))
        res shouldBe List(1, 2)
      }
    }

    describe("equals") {
      it("should return false when two values are different") {
        left("one").equals(right(42)) shouldEqual false
        right(42).equals(left("one")) shouldEqual false
        right(42).equals(right(24)) shouldEqual false
        left("one").equals(left("two")) shouldEqual false
      }

      it("should return true when two values are equals") {
        left("one").equals(left("one")) shouldEqual true
        right(42).equals(right(42)) shouldEqual true
      }
    }

    describe("partitionEithers") {
      it("should return the empty list for empty list") {
        partitionEithers(List.empty[Either[String, Int]]) shouldBe ((List.empty, List.empty))
      }

      it("should partition the list between lefts and rights") {
        val xs = List[Either[String, Int]](left("foo"), right(3), left("bar"), right(7), left("baz"))
        partitionEithers(xs) shouldBe ((List("foo", "bar", "baz"), List(3, 7)))
      }
    }

    describe("Show[Either]") {
      it("should be an instance of the typeclass") {
        import io.hascalator.typeclasses.Show.ops._
        leftOne.show shouldBe """Left("one")"""
        right42.show shouldBe "Right(42)"
      }
    }

    describe("Eq[Either]") {
      it("should be an instance of the typeclass") {
        val eqInstance = implicitly[Eq[Either[String, Int]]]

        eqInstance.eq(leftOne, leftOne) shouldBe true
        eqInstance.eq(right42, right42) shouldBe true
        eqInstance.eq(right42, leftOne) shouldBe false
        eqInstance.eq(leftOne, right42) shouldBe false
      }
    }

    describe("Ord[Either]") {
      it("should be an instance of the typeclass") {
        val ordInstance = implicitly[Ord[Either[String, Int]]]

        ordInstance.compare(leftOne, leftOne) shouldBe Ordering.EQ
        ordInstance.compare(leftTwo, leftOne) shouldBe Ordering.GT
        ordInstance.compare(right42, right42) shouldBe Ordering.EQ
        ordInstance.compare(right41, right42) shouldBe Ordering.LT
        ordInstance.compare(right42, leftOne) shouldBe Ordering.GT
        ordInstance.compare(leftOne, right42) shouldBe Ordering.LT
      }
    }
  }
}

trait EitherValues {
  val leftOne: Either[String, Int] = Either.left("one")
  val leftTwo: Either[String, Int] = Either.left("two")
  val right42: Either[String, Int] = Either.right(42)
  val right41: Either[String, Int] = Either.right(41)
}