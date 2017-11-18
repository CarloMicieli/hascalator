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

class QueueSpec extends AbstractTestSpec with QueueFixture {
  describe("A Queue") {
    describe("enqueue") {
      it("should increase the queue size by 1") {
        (emptyQueue enqueue 42).size shouldBe 1
      }

      it("should be the first element in an empty queue") {
        (emptyQueue enqueue 999).peek shouldBe Maybe.just(999)
      }

      it("should be the last element in a queue") {
        (queue enqueue 999).peek shouldBe Maybe.just(1)
      }
    }

    describe("dequeue") {
      it("should return a left value for empty queues") {
        emptyQueue.dequeue.isLeft shouldBe true
      }

      it("should return the value and the new queue with the element removed") {
        val (x, nq) = queue.dequeue.get
        x shouldBe 1
        nq.size shouldBe 2
        nq.peek shouldBe Maybe.just(2)
      }
    }

    describe("peek") {
      it("should return none for empty queues") {
        emptyQueue.peek shouldBe Maybe.none
      }

      it("should return the first element from non empty queues") {
        queue.peek shouldBe Maybe.just(1)
      }
    }

    describe("isEmpty") {
      it("should return true for empty queues") {
        emptyQueue.isEmpty shouldBe true
      }

      it("should return false for empty queues") {
        queue.isEmpty shouldBe false
      }
    }

    describe("size") {
      it("should be 0 for empty Queues") {
        emptyQueue.size shouldBe 0
      }

      it("should count the number of elements") {
        queue.size shouldBe 3
      }
    }
  }
}

trait QueueFixture {
  def emptyQueue: Queue[Int] = BalancedQueue.empty[Int]
  def queue: Queue[Int] = emptyQueue.enqueue(1).enqueue(2).enqueue(3)
}