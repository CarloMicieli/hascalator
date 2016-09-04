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

import io.hascalator.AbstractTestSuite

class StackOpTestSuite extends AbstractTestSuite with SampleStacks {
  "A sequence of valid ops" should "produce valid stacks" in {
    import StackOp._
    val res = sequence(
      emptyStack,
      List(PushOp(1), PushOp(2), PushOp(3), PopOp, PopOp, PushOp(5))
    )

    val Right(finalStack) = res
    finalStack.isEmpty shouldBe false
    finalStack.size shouldBe 2
    finalStack.top shouldBe Just(5)
  }

  "A sequence of invalid operation" should "produce an Bad result" in {
    import StackOp._

    val res = sequence(
      emptyStack,
      List(PushOp(1), PushOp(2), PushOp(3), PopOp, PopOp, PopOp, PopOp, PushOp(5))
    )

    val Left(invalidOp) = res
    invalidOp.op shouldBe PopOp
    invalidOp.ex.getMessage shouldBe "Stack is empty"
    invalidOp.stack.isEmpty shouldBe true
  }
}

trait SampleStacks {
  def emptyStack: Stack[Int] = Stack.empty[Int]
}
