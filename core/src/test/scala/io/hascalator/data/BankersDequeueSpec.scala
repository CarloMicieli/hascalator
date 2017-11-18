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

class BankersDequeueSpec extends AbstractTestSpec with BankersDequeueFixture {
  describe("length") {
    it("should be 0 for empty Dequeues") {
      emptyDequeue.length shouldBe 0
    }

    it("should be 1 for singleton Dequeues") {
      singletonDequeue(42).length shouldBe 1
    }

    it("should be the number of items for non empty Dequeues") {
      nonEmptyDequeue(1, 2, 3).length shouldBe 3
    }
  }

  describe("reverse") {
    it("should produce an empty dequeue from the empty one") {
      emptyDequeue.reverse shouldBe emptyDequeue
    }

    it("should reverse the dequeue") {
      val reversed = nonEmptyDequeue(1, 2, 3).reverse
      reversed.length shouldBe 3
      reversed.first shouldBe Maybe.just(3)
      reversed.last shouldBe Maybe.just(1)
    }
  }

  describe("isEmpty") {
    it("should be true for empty Dequeues") {
      emptyDequeue.isEmpty shouldBe true
    }

    it("should be false for singleton Dequeues") {
      singletonDequeue(42).isEmpty shouldBe false
    }

    it("should be false for non empty Dequeues") {
      nonEmptyDequeue(1, 2, 3).isEmpty shouldBe false
    }
  }

  describe("first") {
    it("should return a none for empty Dequeues") {
      emptyDequeue.first shouldBe Maybe.none[Int]
    }

    it("should return a just value with the first element from singleton Dequeues") {
      singletonDequeue(42).first shouldBe Maybe.just(42)
    }

    it("should return a just value with the first element from non empty Dequeues") {
      nonEmptyDequeue(42, 4, 66).first shouldBe Maybe.just(42)
    }
  }

  describe("last") {
    it("should return a none for empty Dequeues") {
      emptyDequeue.last shouldBe Maybe.none[Int]
    }

    it("should return a just value with the last element from singleton Dequeues") {
      singletonDequeue(42).last shouldBe Maybe.just(42)
    }

    it("should return a just value with the last element from non empty Dequeues") {
      nonEmptyDequeue(42, 4, 66).last shouldBe Maybe.just(66)
    }
  }

  describe("popFront") {
    it("should return a none for empty Dequeues") {
      emptyDequeue.popFront shouldBe Maybe.none[(Int, BankersDequeue[Int])]
    }

    it("should pop the first element from non empty Dequeues") {
      val Just((f, ndq)) = nonEmptyDequeue(1, 6, 3).popFront
      f shouldBe 1
      ndq.length shouldBe 2
      ndq.first shouldBe Maybe.just(6)
    }
  }

  describe("popBack") {
    it("should return a none for empty Dequeues") {
      emptyDequeue.popBack shouldBe Maybe.none[(Int, BankersDequeue[Int])]
    }

    it("should pop the last element from non empty Dequeues") {
      val Just((l, ndq)) = nonEmptyDequeue(1, 2, 3).popBack
      l shouldBe 3
      ndq.length shouldBe 2
      ndq.last shouldBe Maybe.just(2)
    }
  }

  describe("pushFront") {
    it("should add a new element in front of empty Dequeues") {
      val ndq = emptyDequeue.pushFront(42)
      ndq.isEmpty shouldBe false
      ndq.first shouldBe Maybe.just(42)
    }

    it("should add a new element in front of non empty Dequeues") {
      val ndq = nonEmptyDequeue(1, 3, 6).pushFront(42)
      ndq.first shouldBe Maybe.just(42)
    }
  }

  describe("pushBack") {
    it("should add a new element in front of empty Dequeues") {
      val ndq = emptyDequeue.pushBack(42)
      ndq.isEmpty shouldBe false
      ndq.last shouldBe Maybe.just(42)
    }

    it("should add a new element in front of non empty Dequeues") {
      val ndq = nonEmptyDequeue(1, 3, 6).pushBack(42)
      ndq.last shouldBe Maybe.just(42)
    }
  }

  describe("takeFront") {
    it("should return the empty list take from the front of the empty dequeue") {
      emptyDequeue.takeFront(1) shouldBe List.empty[Int]
    }

    it("should return the first elements in a list take from the front of the non empty dequeue") {
      nonEmptyDequeue(1, 2, 3).takeFront(1) shouldBe List(1)
    }
  }

  describe("takeBack") {
    it("should return the empty list take from the back of the empty dequeue") {
      emptyDequeue.takeBack(1) shouldBe List.empty[Int]
    }

    it("should return the last elements in a list take from the back of the non empty dequeue") {
      nonEmptyDequeue(1, 2, 3).takeBack(2) shouldBe List(3, 2)
    }
  }

  describe("toList") {
    it("should return the empty list from the empty dequeue") {
      emptyDequeue.toList shouldBe List.empty[Int]
    }

    it("should produce a list with the dequeue items") {
      nonEmptyDequeue(2, 3, 5).toList shouldBe List(2, 3, 5)
    }
  }
}

trait BankersDequeueFixture {
  def emptyDequeue: BankersDequeue[Int] = BankersDequeue.empty[Int]

  def singletonDequeue(x: Int): BankersDequeue[Int] = {
    BankersDequeue.fromList(List(x))
  }

  def nonEmptyDequeue(x: Int, y: Int, z: Int): BankersDequeue[Int] = {
    BankersDequeue.fromList(List(x, y, z))
  }
}
