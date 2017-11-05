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
package dst

import Prelude._
import Maybe._
import scala.NoSuchElementException

import io.hascalator.AbstractTestSpec

class RBTreeSpec extends AbstractTestSpec with RBTreesFixture {
  describe("A Red-Black tree") {
    describe("size") {
      it("should be 0 for empty RB trees") {
        val empty = RBTree.empty[Int, String]
        empty.size shouldBe 0
      }

      it("should count the number of nodes in a RB tree") {
        tree.size shouldBe 4
      }
    }

    describe("isEmpty") {
      it("should return 'true' for the empty RB tree") {
        emptyTree.isEmpty shouldBe true
      }

      it("should return 'false' for non empty RB trees") {
        tree.isEmpty shouldBe false
      }
    }

    describe("insert") {
      it("should add the new element as the root, when the starting RB tree is empty") {
        val t = emptyTree.insert(42, "answer")
        t.get shouldBe ((42, "answer"))
      }

      it("should increase the new RB tree size by 1") {
        val t = tree.insert(1, "one")
        t should have size (tree.size + 1L)
        t.lookup(1) shouldBe just("one")
      }

      ignore("should replace the value, if the RB tree already contains the key") {
        val t = tree.insert(42, "answer")
        t.lookup(42) shouldBe just("answer")
      }
    }

    describe("toList") {
      it("should convert the empty RB tree to the empty list") {
        emptyTree.toList shouldBe List()
      }

      it("should convert a RB tree to a list") {
        tree.toList shouldBe List((21, "b"), (42, "a"), (66, "f"), (99, "c"))
      }
    }

    describe("lookup") {
      it("should return a None searching an element in the empty RB tree") {
        emptyTree.lookup(99) shouldBe none
      }

      it("should return a Just when the element exists in a non empty RB tree") {
        tree.lookup(42) shouldBe just("a")
      }

      it("should return a None when the element doesn't exist in a non empty RB tree") {
        tree.lookup(-1) shouldBe none
      }
    }

    describe("min") {
      it("should return a None as the minimum value from the empty RB tree") {
        emptyTree.min shouldBe none
      }

      it("should return a Just with the minimum key in a non empty RB tree") {
        tree.min shouldBe just(21)
      }
    }

    describe("max") {
      it("should return a None as the maximum value from the empty RB tree") {
        emptyTree.max shouldBe none
      }

      it("should return a Just with the maximum key in a non empty RB tree") {
        tree.max shouldBe just(99)
      }
    }

    describe("contains") {
      it("should return false, when checking the empty RB tree") {
        emptyTree.contains(99) shouldBe false
      }

      it("should return true when the RB tree contains the key") {
        tree.contains(99) shouldBe true
      }

      it("should return false, when the RB tree doesn't contain the key") {
        tree.contains(9999) shouldBe false
      }
    }

    describe("depth") {
      it("should return 0 for the empty RB tree") {
        emptyTree.depth shouldBe 0
      }

      it("should count the longest path in the RB tree") {
        tree.depth shouldBe 3
      }
    }

    describe("fold") {
      it("should apply a function to all RB tree values") {
        numTree.fold(_ + _) shouldBe 15
      }

      it("should throw an exception for the empty RB tree") {
        the[NoSuchElementException] thrownBy {
          emptyTree.fold(_ + _)
        } should have message "fold: tree is empty"
      }
    }

    describe("toString") {
      it("should produce a string representation for the empty RB tree") {
        emptyTree.toString shouldBe "-"
      }

      it("should produce a string representation for RB trees") {
        tree.toString shouldBe "(((- [21->b] -) [42->a] -) [66->f] (- [99->c] -))"
      }
    }
  }
}

trait RBTreesFixture {
  def emptyTree: Tree[Int, String] = RBTree.empty[Int, String]
  def tree: Tree[Int, String] = RBTree.fromList(List((42, "a"), (21, "b"), (99, "c"), (66, "f")))
  def numTree: Tree[Int, Int] = RBTree.fromList(List((1, 1), (2, 2), (3, 3), (4, 4), (5, 5)))
}