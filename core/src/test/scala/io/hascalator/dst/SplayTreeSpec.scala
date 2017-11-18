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

class SplayTreeSpec extends AbstractTestSpec with SampleSplayTrees {
  describe("A SplayTree") {
    describe("empty") {
      it("should create an empty Splay tree") {
        SplayTree.empty[Int].isEmpty shouldEqual true
      }
    }

    describe("fromList") {
      it("should create an empty Splay tree from the empty list") {
        val tree = SplayTree.fromList(List.empty[Int])
        tree.isEmpty shouldEqual true
      }

      it("should create a Splay tree from a list") {
        val tree = SplayTree.fromList(List(42, 15, 67, 109, 4, 55))
        tree.size shouldEqual 6
        tree.isEmpty shouldEqual false
        tree.top shouldEqual Maybe.just(4)
      }
    }

    describe("pop") {
      it("should remove min element from a Splay tree") {
        val (m, tree) = splayTree.pop
        m shouldEqual 4
        tree.top shouldEqual Maybe.just(15)
      }
    }

    describe("merge") {
      it("should merge two empty Splay trees") {
        val tree = emptySplayTree merge emptySplayTree
        tree.isEmpty shouldEqual true
      }

      it("should merge two Splay trees") {
        val tree = splayTree merge emptySplayTree
        tree.top shouldEqual splayTree.top
      }
    }

    describe("insert") {
      it("should create a singleton Splay tree adding a key to the empty tree") {
        val tree = emptySplayTree insert 42
        tree.isEmpty shouldEqual false
      }

      it("should increase the size") {
        val tree = emptySplayTree insert 42
        tree.size shouldEqual 1
      }
    }

    describe("isEmpty") {
      it("should return true for empty Splay trees") {
        emptySplayTree.isEmpty shouldEqual true
      }
    }
  }
}

trait SampleSplayTrees {
  val emptySplayTree: SplayTree[Int] = SplayTree.empty[Int]

  def singleton(x: Int): SplayTree[Int] = SplayTree.fromList(List(x))

  val splayTree: SplayTree[Int] = SplayTree.fromList(List(42, 15, 67, 109, 4, 55))
}