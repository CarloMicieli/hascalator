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

import io.hascalator.AbstractTestSpec

class StackSpec extends AbstractTestSpec with StacksFixture {

  describe("A Stack, backed by a list") {
    describe("size") {
      it("should be 0 for the empty stack") {
        val emptyStack = Stack.empty[Int]
        emptyStack.size shouldBe 0
      }

      it("should be the number of elements for a non empty stack") {
        nonEmptyStack.size shouldBe 6
      }
    }

    describe("isEmpty") {
      it("should return 'true' for the empty stack") {
        emptyStack.isEmpty shouldBe true
      }

      it("should return 'false' for the empty stack") {
        nonEmptyStack.isEmpty shouldBe false
      }
    }

    describe("nonEmpty") {
      it("should return 'false' for the empty stack") {
        emptyStack.nonEmpty shouldBe false
      }

      it("should return 'true' for the empty stack") {
        nonEmptyStack.nonEmpty shouldBe true
      }
    }

    describe("push") {
      it("should create a new Stack with the new element") {
        val newStack = emptyStack.push(99)
        newStack.size shouldBe 1
        newStack.isEmpty shouldBe false
        newStack.top shouldBe Maybe.just(99)
      }

      it("should add elements in the LIFO way") {
        val stack = emptyStack.push(1).push(2).push(3)
        stack.size shouldBe 3
        val Right((x, _)) = stack.pop
        x shouldBe 3
      }
    }

    describe("pop") {
      it("should return a 'Bad' value popping elements out of the empty stack") {
        val x = emptyStack.pop
        x.isLeft shouldBe true
      }

      it("should return a 'Good' value with a pair with the popped element and resulting stack") {
        val Right((x, newStack)) = nonEmptyStack.pop
        x shouldBe 15
        newStack.size shouldBe nonEmptyStack.size - 1
      }
    }

    describe("popUntil") {
      it("should return a pair with an empty result for the empty stack") {
        val (removed, newStack) = emptyStack.popUntil(_ % 2 == 0)
        removed.isEmpty shouldBe true
        newStack.isEmpty shouldBe true
      }

      it("should return the removed elements") {
        val (removed, newStack) = nonEmptyStack.popUntil(_ != 42)
        removed.length shouldBe 2
        removed shouldBe List(15, 41)
        newStack.size shouldBe nonEmptyStack.size - 2
        newStack.top shouldBe Maybe.just(42)
      }
    }

    describe("top") {
      it("should return a 'None' for the empty stack") {
        emptyStack.top shouldBe None
      }

      it("should return a 'Just' with the top element for the non empty stack") {
        nonEmptyStack.top shouldBe Maybe.just(15)
      }
    }

    describe("toString") {
      it("should produce a string representation for the empty stack") {
        emptyStack.toString shouldBe "<emptystack>"
      }

      it("should produce a string representation for the non empty stack") {
        nonEmptyStack.toString shouldBe "<stack:top = 15>"
      }
    }
  }
}

trait StacksFixture {
  def emptyStack: Stack[Int] = Stack.empty[Int]

  def nonEmptyStack: Stack[Int] = {
    val s = Stack.empty[Int]
    s.push(1).push(2).push(3).push(42).push(41).push(15)
  }
}
