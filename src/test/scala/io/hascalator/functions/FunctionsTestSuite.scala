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

package io.hascalator.functions

import io.hascalator.{ AbstractTestSuite, ApplicationException }

class FunctionsTestSuite extends AbstractTestSuite {

  "id" should "return the same value" in {
    id(42) shouldBe 42
  }

  "A const function" should "always return the same value" in {
    val ConstVal: Int = 42
    val f: String => Int = const(ConstVal)
    f("hello") shouldBe ConstVal
    f("world") shouldBe ConstVal
  }

  "flip(f)" should "flip the function arguments" in {
    val sum: (Int, String) => String = (n, s) => s"$s($n)"
    flip(sum)("Hello", 42) shouldBe sum(42, "Hello")
  }

  "flip(flip(f))" should "produce the original function" in {
    val sum: (Int, String) => String = (n, s) => s"$s($n)"
    flip(flip(sum))(42, "Hello") shouldBe sum(42, "Hello")
  }

  "error" should "throw an exception" in {
    the[ApplicationException] thrownBy {
      error("BOOM")
    } should have message "*** Exception: 'BOOM'"
  }
}
