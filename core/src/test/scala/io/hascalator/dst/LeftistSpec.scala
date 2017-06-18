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
package dst

import Prelude._

class LeftistSpec extends AbstractTestSpec with SampleLeftistTrees {

  describe("A Leftist") {
    describe("rank") {
      it("should be 0 for empty nodes") {
        Leftist.empty[Int].rank shouldEqual 0
      }

      it("should be 1 for single nodes") {
        Leftist.singleton(42).rank shouldEqual 1
      }
    }

    describe("top") {
      it("should return a None from the empty Leftist") {
        emptyTree.top shouldEqual Maybe.none[Int]
      }

      it("should return the only element in a singleton Leftist") {
        singleton(42).top shouldEqual Maybe.just(42)
      }
    }

    describe("insert") {
      it("should insert a new element in a empty tree") {
        val t = emptyTree insert 42
        t.size shouldEqual 1
        t.isEmpty shouldEqual false
        t.top shouldEqual Maybe.just(42)
      }

      it("should insert a new element in a tree") {
        val t = Leftist.fromList(List(10, 2, 30))
        val t2 = t insert 1
        t2.size shouldEqual 4
        t2.top shouldEqual Maybe.just(1)
      }
    }

    describe("merge") {
      it("should merge two Leftist trees") {
        val t1 = Leftist.fromList(List(85, 57, 52, 81, 16))
        val t2 = Leftist.fromList(List(8, 18, 31))

        val tree = t1 merge t2
        tree.size shouldEqual (t1.size + t2.size)
        tree.top shouldEqual Maybe.just(8)
      }
    }

    describe("pop") {
      it("should return a Left popping the top element from the empty tree") {
        emptyTree.pop.isLeft shouldEqual true
      }

      it("should return the top element and a empty Leftist tree popping the element from a singleton tree") {
        val r = singleton(42).pop.getOrElse((-1, emptyTree))
        val expected = (42, emptyTree)
        r shouldEqual expected
      }
    }

    describe("fromList") {
      it("should create empty tree from empty lists") {
        val t = Leftist.fromList(List.empty[Int])
        t.isEmpty shouldEqual true
      }

      it("should create a tree from a list") {
        val xs = List(99, 29, 92, 91, 26, 24, 47, 56, 76, 63)
        val tree = Leftist.fromList(xs)
        tree.size shouldEqual xs.length
        tree.top shouldEqual Maybe.just(24)
      }
    }
  }
}

trait SampleLeftistTrees {
  val emptyTree: Leftist[Int] = Leftist.empty[Int]

  def singleton(x: Int): Leftist[Int] = Leftist.singleton(x)
}