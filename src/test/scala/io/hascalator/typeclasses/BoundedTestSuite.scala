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

package io.hascalator.typeclasses

import io.hascalator.AbstractTestSuite

class BoundedTestSuite extends AbstractTestSuite {
  "Boolean" should "be an instance of the Bounded typeclass" in {
    val b = Bounded[Boolean]
    b.maxBound shouldBe true
    b.minBound shouldBe false
  }

  "Char" should "be an instance of the Bounded typeclass" in {
    val b = Bounded[Char]
    b.maxBound shouldBe Char.MaxValue
    b.minBound shouldBe Char.MinValue
  }

  "Short" should "be an instance of the Bounded typeclass" in {
    val b = Bounded[Short]
    b.maxBound shouldBe Short.MaxValue
    b.minBound shouldBe Short.MinValue
  }

  "Int" should "be an instance of the Bounded typeclass" in {
    val b = Bounded[Int]
    b.maxBound shouldBe Int.MaxValue
    b.minBound shouldBe Int.MinValue
  }

  "Long" should "be an instance of the Bounded typeclass" in {
    val b = Bounded[Long]
    b.maxBound shouldBe Long.MaxValue
    b.minBound shouldBe Long.MinValue
  }

  "Float" should "be an instance of the Bounded typeclass" in {
    val b = Bounded[Float]
    b.maxBound shouldBe Float.MaxValue
    b.minBound shouldBe Float.MinValue
  }

  "Double" should "be an instance of the Bounded typeclass" in {
    val b = Bounded[Double]
    b.maxBound shouldBe Double.MaxValue
    b.minBound shouldBe Double.MinValue
  }
}
