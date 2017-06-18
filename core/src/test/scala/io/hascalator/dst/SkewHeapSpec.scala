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

import io.hascalator.AbstractTestSpec

class SkewHeapSpec extends AbstractTestSpec with SampleSkewHeaps {
  describe("A Skew Heap") {
    describe("isEmpty") {
      it("should be 'true' for the empty skew heap") {
        val empty = SkewHeap.empty[Int]
        empty.isEmpty shouldBe true
      }

      it("should be 'false' when the skew heap is not empty") {
        val heap = SkewHeap.empty[Int].insert(42)
        heap.isEmpty shouldBe false
      }
    }

    describe("top") {
      it("should return a None for the empty SkewHeap") {
        emptyHeap.top shouldEqual Maybe.none
      }

      it("should return a Just with the minimum value from a non empty SkewHeap") {
        heap.top shouldEqual Maybe.just(15)
      }
    }

    describe("insert") {
      it("should insert a new key in a empty SkewHeap") {
        val h = emptyHeap.insert(42)
        h.isEmpty shouldEqual false
        h.size shouldEqual 1
        h.top shouldEqual Maybe.just(42)
      }

      it("should insert a new key in a non empty SkewHeap") {
        val h = heap.insert(42)
        h.isEmpty shouldEqual false
        h.size shouldEqual (heap.size + 1)
        h.top shouldEqual Maybe.just(15)
      }

      it("should insert a new minimum key in a non empty SkewHeap") {
        val h = heap.insert(-1)
        h.top shouldEqual Maybe.just(-1)
      }
    }

    describe("size") {
      it("should be 0 for the empty skew heap") {
        emptyHeap.size shouldBe 0
      }

      it("should be the number of entries in the skew heap") {
        heap.size shouldBe 5
      }
    }

    describe("min") {
      it("should return None for the empty skew heap") {
        emptyHeap.top shouldBe Maybe.none
      }

      it("should return Just with the entry for the minimum key") {
        heap.top shouldBe Maybe.just(15)
      }
    }

    describe("removeMin") {
      it("should return the empty skew heap if it's empty") {
        emptyHeap.removeMin shouldBe emptyHeap
      }

      it("should return a new skew heap without the minimum key entry") {
        heap.removeMin.size shouldBe heap.size - 1
      }
    }

    describe("merge") {
      it("should create a new SkewHeap with size the sum of the two original heaps") {
        val h1 = emptyHeap merge heap
        val h2 = heap merge emptyHeap

        h1.size shouldEqual h2.size
        h1.top shouldEqual h2.top
      }

      it("should create a new SkewHeap with the minimum of the two top original values") {
        val h1 = SkewHeap.fromList(List(6, 125, 34, 76))
        val h2 = SkewHeap.fromList(List(22, 0, 546))

        (h1 merge h2).top shouldEqual Maybe.just(0)
      }
    }
  }
}

trait SampleSkewHeaps {
  val emptyHeap: SkewHeap[Int] = SkewHeap.empty[Int]

  val heap: SkewHeap[Int] = SkewHeap.fromList(List(42, 36, 66, 15, 99))
}
