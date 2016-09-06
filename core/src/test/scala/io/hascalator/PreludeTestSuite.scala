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

import Prelude._

class PreludeTestSuite extends AbstractTestSuite {

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
    sum.flip("Hello", 42) shouldBe sum(42, "Hello")
  }

  "flip(flip(f))" should "produce the original function" in {
    val sum: (Int, String) => String = (n, s) => s"$s($n)"
    flip(flip(sum))(42, "Hello") shouldBe sum(42, "Hello")
  }

  "error" should "throw an exception" in {
    the[ApplicationException] thrownBy {
      error("BOOM")
    } should have message "*** Exception: BOOM"
  }

  "errorWithoutStackTrace" should "throw an exception" in {
    the[ApplicationException] thrownBy {
      errorWithoutStackTrace("BOOM")
    } should have message "*** Exception: BOOM"
  }

  "util" should "apply the function f until condition holds" in {
    until[Int](_ > 10)(_ + 1)(1) shouldBe 11
    until[Int](_ < 10)(_ + 1)(1) shouldBe 1
  }

  "undefined" should "always throw an exception" in {
    the[NotImplementedError] thrownBy {
      undefined
    } should have message "an implementation is missing"
  }

  "fst" should "return the first element of a pair" in {
    fst((1, 2)) shouldBe 1
  }

  "snd" should "return the second element of a pair" in {
    snd((1, 2)) shouldBe 2
  }
}
