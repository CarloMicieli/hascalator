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

class ListBuilderSpec extends AbstractTestSpec {
  describe("ListBuilder") {
    it("should create the empty list when no element was added") {
      val builder = new ListBuilder[Int]
      builder.result() shouldBe List()
    }

    it("should add elements in order of insertion") {
      val builder = new ListBuilder[Int]
      builder += 1
      builder += 2
      builder += 3
      builder.result() shouldBe List(1, 2, 3)
    }

    it("should only add elements after clear is invoked") {
      val builder = new ListBuilder[Int]
      builder += 1
      builder += 2
      builder += 3
      builder.clear()
      builder += 4
      builder += 5
      builder.result() shouldBe List(4, 5)
    }
  }
}
