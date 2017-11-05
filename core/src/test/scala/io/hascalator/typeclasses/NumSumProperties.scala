/*
 * Copyright 2016 CarloMicieli
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
package typeclasses

import Prelude._

import io.hascalator.AbstractPropertySpec
import org.scalacheck.Prop.forAll

class NumSumProperties extends AbstractPropertySpec with AdditionLaws {
  property("sum of Ints is lawful according to addition laws") {
    check(forAll { (x: Int, y: Int, z: Int) =>
      commutativityLaw[Int](x, y)
      associativityLaw[Int](x, y, z)
      identityElement[Int](x)
    })
  }

  property("sum of Longs is lawful according to addition laws") {
    check(forAll { (x: Long, y: Long, z: Long) =>
      commutativityLaw[Long](x, y)
      associativityLaw[Long](x, y, z)
      identityElement[Long](x)
    })
  }
}
