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

package io.hascalator.dst

import io.hascalator.data.List
import io.hascalator.data.Maybe._
import io.hascalator.{ AbstractTestSpec, ApplicationException }

class BinarySearchTreeSpec extends AbstractTestSpec with BinarySearchTreesFixture {
  describe("An immutable BS tree") {
    describe("size") {
      it("should return 0 for empty tree") {
        emptyTree.size shouldBe 0
      }

      it("should return the number of elements in a tree") {
        tree.size shouldBe 4
      }
    }
  }

  describe("isEmpty") {
    it("should return true for empty trees") {
      emptyTree.isEmpty shouldBe true
    }

    it("should return false for non empty trees") {
      tree.isEmpty shouldBe false
    }
  }

  describe("fromList") {
    it("should create a BS tree from a list") {
      val xs = List((42, "a"), (21, "b"), (99, "c"), (66, "f"))
      val t = BinarySearchTree.fromList(xs)
      t.size shouldBe xs.length
      t.toString shouldBe "((- [21->b] -) [42->a] ((- [66->f] -) [99->c] -))"
      withClue("The tree root should be the list head") {
        t.get shouldBe xs.head
      }
    }
  }

  describe("insert") {
    it("should insert the new element at the root, inserting into an empty tree") {
      val t = emptyTree.insert(42, "answer")
      t should have size 1
      t.get shouldBe ((42, "answer"))
    }

    it("should create a new tree with the new element") {
      val t = tree.insert(55, "x")
      t should have size (tree.size + 1L)
      t.contains(55) shouldBe true
    }

    it("should increase tree size") {
      val t = emptyTree.insert(42, "answer").insert((1, "one"))
      t should have size 2
      t.isEmpty shouldBe false
    }

    it("should replace the value, if the tree already contains the key") {
      val t = tree.insert(42, "answer")
      t.lookup(42) shouldBe just("answer")
    }
  }

  describe("lookup") {
    it("should return a Just, if the element is found") {
      tree.lookup(99) shouldBe just("c")
    }

    it("should return a None, if the element is not found") {
      tree.lookup(-1) shouldBe none
    }
  }

  describe("depth") {
    it("should have a 0 depth if the tree is empty") {
      emptyTree.depth shouldBe 0
    }

    it("should return the number of levels in a tree") {
      tree.depth shouldBe 3
    }
  }

  describe("min") {
    it("should return None, if the tree is empty") {
      emptyTree.min shouldBe none
    }

    it("should return a Just value, with the element with the minimum key") {
      tree.min shouldBe just(21)
    }
  }

  describe("max") {
    it("should return None, if the tree is empty") {
      emptyTree.max shouldBe none
    }

    it("should return a Just value, with the element with the maximum key") {
      tree.max shouldBe just(99)
    }
  }

  describe("toList") {
    it("should return an empty list for the empty tree") {
      emptyTree.toList shouldBe List()
    }

    it("should return a list with the tree elements, as pairs") {
      tree.toList shouldBe List((21, "b"), (42, "a"), (66, "f"), (99, "c"))
    }
  }

  describe("upsert") {
    it("should update a value for a key already in the tree") {
      val t = tree.upsert(21, "b")(_ * 2)
      t.lookup(21) shouldBe just("bb")
    }

    it("should insert a new value, if the key is not in tree") {
      val t = tree.upsert(45, "d")(_ * 2)
      t.lookup(45) shouldBe just("d")
    }
  }

  describe("delete") {
    it("should return an empty result, deleting from the empty tree") {
      val (removed, newTree) = emptyTree.delete(4)
      removed shouldBe none
      newTree shouldBe BinarySearchTree.empty[Int, String]
    }

    it("should return a pair with deleted value and the new tree without the removed key") {
      val (removed, newTree) = tree.delete(21)
      removed shouldBe just("b")
      newTree.toString shouldBe "(- [42->a] ((- [66->f] -) [99->c] -))"
    }

    it("should remove the tree root") {
      val (removed, newTree) = tree.delete(42)
      removed shouldBe just("a")
      newTree.toString shouldBe "((- [21->b] -) [66->f] (- [99->c] -))"
    }
  }

  describe("toString") {
    it("should produce a string representation for the empty tree") {
      emptyTree.toString shouldBe "-"
    }

    it("should produce a string representation for trees") {
      tree.toString shouldBe "((- [21->b] -) [42->a] ((- [66->f] -) [99->c] -))"
    }
  }

  describe("map") {
    it("should do nothing, applying a function to the empty tree") {
      val t: Tree[Int, String] = emptyTree.map(_ * 2)
      t.isEmpty shouldBe true
    }

    it("should apply a function to every tree value") {
      val t = tree.map(_ * 2)
      t.toString shouldBe "((- [21->bb] -) [42->aa] ((- [66->ff] -) [99->cc] -))"
    }
  }

  describe("contains") {
    it("should return always false for the empty tree") {
      emptyTree.contains(42) shouldBe false
    }

    it("should return true if the element is in the tree") {
      tree.contains(42) shouldBe true
    }

    it("should return false if the element is in the tree") {
      tree.contains(-1) shouldBe false
    }
  }

  describe("fold") {
    it("should throw an exception folding an empty tree") {
      the[ApplicationException] thrownBy {
        emptyTree.fold(_ + _)
      } should have message "*** Exception: 'fold: tree is empty'"
    }

    it("should fold the tree applying a function") {
      numbersTree.fold(_ + _) shouldBe 1 + 2 + 3 + 4
      singleNumberTree.fold(_ + _) shouldBe 1
    }
  }
}

trait BinarySearchTreesFixture {
  import BinarySearchTree._
  val emptyTree: Tree[Int, String] = empty[Int, String]
  val tree: Tree[Int, String] = fromList(List((42, "a"), (21, "b"), (99, "c"), (66, "f")))
  val numbersTree: Tree[Int, Int] = fromList(List((1, 1), (2, 2), (3, 3), (4, 4)))
  val singleNumberTree: Tree[Int, Int] = fromList(List((1, 1)))
}