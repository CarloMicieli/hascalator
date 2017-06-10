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

class NonEmptySpec extends AbstractTestSpec with SampleNonEmptyLists {
  describe("A NonEmpty") {
    describe("unfold") {
      it("should generate the singleton NonEmpty list when generator function return a None") {
        NonEmpty.unfold(42)(x => ("one", Maybe.none)) shouldBe NonEmpty("one")
      }

      it("should generate NonEmpty lists") {
        val f: Int => (String, Maybe[Int]) = {
          case 1 => ("I", Maybe.just(2))
          case 2 => ("II", Maybe.just(3))
          case 3 => ("III", Maybe.just(4))
          case 4 => ("IV", Maybe.none)
        }

        NonEmpty.unfold(1)(f) shouldBe NonEmpty("I", "II", "III", "IV")
      }
    }

    describe("singleton") {
      it("should create a new NonEmpty list") {
        val l = NonEmpty.singleton(42)
        l.head shouldBe 42
        l.tail shouldBe List.empty[Int]
      }
    }

    describe("fromList") {
      it("should throw an exception when provided an empty list") {
        the[ApplicationException] thrownBy {
          NonEmpty.fromList(List.empty[Int])
        } should have message "*** Exception: NonEmpty.fromList: empty list"
      }

      it("should create a singleton non empty list") {
        val l = NonEmpty.fromList[Int](List(1))
        l.head shouldBe 1
        l.tail shouldBe List.empty[Int]
      }

      it("should create a new non empty list") {
        val l = NonEmpty.fromList[Int](List(1, 2, 3))
        l.head shouldBe 1
        l.tail shouldBe List(2, 3)
      }
    }

    describe("|:") {
      it("should prepend an element to the singleton list") {
        val l = 1 |: NonEmpty.singleton(2)
        l.head shouldBe 1
        l.tail shouldBe List(2)
      }

      it("should prepend an element to a NonEmpty list") {
        val tail = List(2, 3)
        val l = 1 |: NonEmpty.fromList(tail)

        l.head shouldBe 1
        l.tail shouldBe tail
      }
    }

    describe("drop") {
      it("should drop 0 elements from singleton NonEmpty lists") {
        singleton.drop(0) shouldBe List(42)
      }

      it("should drop n elements from singleton NonEmpty lists") {
        singleton.drop(10) shouldBe List.empty
      }

      it("should drop 0 elements from NonEmpty lists") {
        numbersList.drop(0) shouldBe List(1, 2, 3)
      }

      it("should drop n elements from NonEmpty lists") {
        numbersList.drop(2) shouldBe List(3)
      }
    }

    describe("take") {
      it("should take 0 elements from singleton NonEmpty lists") {
        singleton.take(0) shouldBe List.empty[Int]
      }

      it("should take n elements from singleton NonEmpty lists") {
        singleton.take(10) shouldBe List(42)
      }

      it("should take 0 elements from NonEmpty lists") {
        numbersList.take(0) shouldBe List.empty[Int]
      }

      it("should take n elements from NonEmpty lists") {
        numbersList.take(2) shouldBe List(1, 2)
      }
    }

    describe("uncons") {
      it("should return an element and a None for the singleton NonEmpty list") {
        val expected = (42, Maybe.none[Int])
        singleton.unCons shouldBe expected
      }

      it("should return an element and a Just with remaining element for the NonEmpty list") {
        val expected = (1, Maybe.just(NonEmpty(2, 3)))
        numbersList.unCons shouldBe expected
      }
    }

    describe("head") {
      it("should return the head") {
        list(1, 2, 3).head shouldBe 1
      }
    }

    describe("tail") {
      it("should return the tail") {
        list(1, 2, 3).tail shouldBe List(2, 3)
      }
    }

    describe("init") {
      it("should return the empty list for the singleton NonEmpty lists") {
        singleton.init shouldBe List.empty[Int]
      }

      it("should return everything except the last element for NonEmpty lists") {
        numbersList.init shouldBe List(1, 2)
      }
    }

    describe("nonEmpty") {
      it("should return a None when the provided list is empty") {
        NonEmpty.nonEmpty(List.empty[Int]) shouldBe Maybe.none
      }

      it("should return a Just when the provided list is not empty") {
        NonEmpty.nonEmpty(List(1, 2)) shouldBe Maybe.just(NonEmpty(1, 2))
      }
    }

    describe("map") {
      it("should map a function for singleton NonEmpty list") {
        singleton.map(_ * 2) shouldBe NonEmpty(84)
      }

      it("should map a function for NonEmpty list") {
        numbersList.map(_ * 2) shouldBe NonEmpty(2, 4, 6)
      }
    }

    describe("flatMap") {
      it("should flatMap a function for singleton NonEmpty list") {
        val f: Int => NonEmpty[Int] = n => NonEmpty(n, n * 2)
        singleton.flatMap(f) shouldBe NonEmpty(42, 84)
      }

      it("should flatMap a function for NonEmpty list") {
        val f: Int => NonEmpty[Int] = n => NonEmpty(n, n * 2)
        numbersList.flatMap(f) shouldBe NonEmpty(1, 2, 2, 4, 3, 6)
      }
    }

    describe("toList") {
      it("should produce a normal list from a singleton NonEmpty list") {
        singleton.toList shouldBe List(42)
      }

      it("should produce a normal list from a NonEmpty list") {
        numbersList.toList shouldBe List(1, 2, 3)
      }
    }

    describe("toString") {
      it("should produce a string representation for singleton lists") {
        singleton.toString shouldBe "42 :| []"
      }

      it("should produce a string representation for NonEmpty lists") {
        numbersList.toString shouldBe "1 :| [2,3]"
      }
    }

    describe("length") {
      it("should always return 1 for singleton NonEmpty list") {
        singleton.length shouldBe 1
      }

      it("should always return the number of elements for NonEmpty lists") {
        numbersList.length shouldBe 3
      }
    }

    describe("foldLeft") {
      it("should apply a function to singleton NonEmpty lists") {
        singleton.foldLeft(0)(_ + _) shouldBe 42
      }

      it("should apply a function to NonEmpty lists") {
        numbersList.foldLeft(0)(_ + _) shouldBe 6
      }
    }

    describe("foldRight") {
      it("should apply a function to singleton NonEmpty lists") {
        singleton.foldRight(0)(_ + _) shouldBe 42
      }

      it("should apply a function to NonEmpty lists") {
        numbersList.foldRight(0)(_ + _) shouldBe 6
      }
    }

    describe("append") {
      it("should append two NonEmpty lists") {
        (numbersList append numbersList) shouldBe NonEmpty(1, 2, 3, 1, 2, 3)
        (singleton append numbersList) shouldBe NonEmpty(42, 1, 2, 3)
        (numbersList append singleton) shouldBe NonEmpty(1, 2, 3, 42)
        (singleton append singleton) shouldBe NonEmpty(42, 42)
      }
    }

    describe("zip") {
      it("should zip two NonEmpty lists") {
        (numbersList zip numbersList) shouldBe NonEmpty((1, 1), (2, 2), (3, 3))
        (singleton zip numbersList) shouldBe NonEmpty((42, 1))
        (numbersList zip singleton) shouldBe NonEmpty((1, 42))
        (singleton zip singleton) shouldBe NonEmpty((42, 42))
      }
    }

    describe("zipWith") {
      it("should zip two NonEmpty lists") {
        numbersList.zipWith(numbersList)(_ + _) shouldBe NonEmpty(2, 4, 6)
        singleton.zipWith(numbersList)(_ + _) shouldBe NonEmpty(43)
        numbersList.zipWith(singleton)(_ + _) shouldBe NonEmpty(43)
        singleton.zipWith(singleton)(_ + _) shouldBe NonEmpty(84)
      }
    }

    describe("apply") {
      it("should construct a Singleton NonEmpty list when provided one element") {
        NonEmpty("one") shouldBe NonEmpty.singleton("one")
      }
    }
  }
}

trait SampleNonEmptyLists {
  val singleton: NonEmpty[Int] = {
    NonEmpty.singleton(42)
  }

  val numbersList: NonEmpty[Int] = list(1, 2, 3)

  def list(x: Int, y: Int, z: Int): NonEmpty[Int] = {
    NonEmpty.fromList(List(x, y, z))
  }
}

