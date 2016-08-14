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
import Maybe._
import io.hascalator.typeclasses.{ Show, Eq, Ord, Ordering }

class MaybeSpec extends AbstractTestSpec with MaybeValues {
  describe("Maybe") {
    describe("pattern matching") {
      it("should be possible to match the just values") {
        val res = just42 match {
          case Just(v) => v
          case None    => "not-found"
        }
        res shouldBe 42
      }

      it("should be possible to match the none values") {
        val res = noneInt match {
          case None    => -1
          case Just(v) => v
        }
        res shouldBe -1
      }
    }

    describe("show") {
      it("should be an instance of the Show typeclass") {
        import Show.ops._
        just("42").show shouldBe """Just("42")"""
        just('A').show shouldBe "Just('A')"
        none[Int].show shouldBe "None"
      }
    }

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

    describe("get") {
      it("should return the wrapped value for just") {
        just42.get shouldBe 42
      }

      it("should throw an exception for none values") {
        the[ApplicationException] thrownBy {
          noneInt.get
        } should have message "*** Exception: 'Maybe.get: a value doesn't exist'"
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

    describe("isEmpty") {
      it("should return false for none values") {
        noneInt.isEmpty shouldBe true
        noneString.isEmpty shouldBe true
      }

      it("should return false for just values") {
        just42.isEmpty shouldBe false
        justOne.isEmpty shouldBe false
      }
    }

    describe("foreach") {
      it("should never run the side effect for none values") {
        var res = 0
        noneString.foreach(x => res = res + 1)
        res shouldBe 0
      }

      it("should run the side effect once for just values") {
        var res = 0
        just42.foreach(x => res = res + 1)
        res shouldBe 1
      }
    }

    describe("mapOrElse") {
      it("should apply the function to just values") {
        just42.mapOrElse(_ * 2)(just(-1)) shouldBe just(84)
      }

      it("should return the orElse for none values") {
        noneInt.mapOrElse(_ * 2)(just(-1)) shouldBe just(-1)
      }
    }

    describe("fold") {
      it("should apply the function to just values") {
        just42.fold(_ * 2)(-1) shouldBe 84
      }

      it("should return the orElse for none values") {
        noneInt.fold(_ * 2)(-1) shouldBe -1
      }
    }

    describe("toList") {
      it("should return the empty list for none values") {
        noneInt.toList shouldBe List.empty[Int]
      }

      it("should return the singleton list for just values") {
        just42.toList shouldBe List(42)
      }
    }

    describe("catMaybes") {
      it("should return the empty list if the original list contains only none values") {
        catMaybes(List(noneInt, noneInt, noneInt)) shouldBe List.empty[Int]
      }

      it("should return the list with the just values") {
        catMaybes(List(just42, none, none, just42, none)) shouldBe List(42, 42)
      }
    }

    describe("Eq[Maybe]") {
      it("should be instance of the typeclass") {
        val eqInstance = implicitly[Eq[Maybe[Int]]]
        eqInstance.eq(just42, just42) shouldBe true
        eqInstance.eq(noneInt, noneInt) shouldBe true
        eqInstance.eq(just42, noneInt) shouldBe false
        eqInstance.eq(noneInt, just42) shouldBe false
      }
    }

    describe("Ord[Maybe]") {
      it("should be instance of the typeclass") {
        val ordInstance = implicitly[Ord[Maybe[Int]]]
        ordInstance.compare(just42, just42) shouldBe Ordering.EQ
        ordInstance.compare(noneInt, noneInt) shouldBe Ordering.EQ
        ordInstance.compare(just42, noneInt) shouldBe Ordering.GT
        ordInstance.compare(noneInt, just42) shouldBe Ordering.LT
        ordInstance.compare(just41, just42) shouldBe Ordering.LT
      }
    }
  }
}

trait MaybeValues {
  val just42: Maybe[Int] = just(42)
  val just41: Maybe[Int] = just(41)
  val justOne: Maybe[String] = just("one")
  val noneInt: Maybe[Int] = none
  val noneString: Maybe[String] = none
}