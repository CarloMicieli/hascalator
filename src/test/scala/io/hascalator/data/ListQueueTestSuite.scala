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

import io.hascalator.AbstractTestSuite

class ListQueueTestSuite extends AbstractTestSuite with ListQueueFixture {
  "A ListQueue" should "have size 0 when is empty" in {
    val q = Queue.empty[Int]
    q.isEmpty shouldBe true
    q.size shouldBe 0
  }

  "enqueue" should "create a new modified queue" in {
    val q = emptyQueue.enqueue(42)
    q.size shouldBe 1
    q.isEmpty shouldBe false
  }

  "dequeue" should "remove the front element from the queue" in {
    val Right((front, q)) = threeQueue.dequeue
    front shouldBe 1
    q.size shouldBe 2
  }

  "dequeue" should "return a Bad value when the queue is empty" in {
    val res = emptyQueue.dequeue
    res.isLeft shouldBe true
  }

  "peek" should "return the front element in the queue" in {
    threeQueue.peek shouldBe Just(1)
    emptyQueue.peek shouldBe None
  }

  "it" should "produce String representation for list queue" in {
    emptyQueue.toString shouldBe "Queue()"
    threeQueue.toString shouldBe "Queue(top = 1)"
  }
}

trait ListQueueFixture {
  def emptyQueue: Queue[Int] = ListQueue.empty[Int]
  def threeQueue: Queue[Int] = emptyQueue.enqueue(1).enqueue(2).enqueue(3)
}