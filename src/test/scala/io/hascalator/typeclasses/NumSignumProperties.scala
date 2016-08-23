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
package typeclasses

import Prelude._

import io.hascalator.AbstractPropertySpec
import org.scalacheck.Prop.forAll

class NumSignumProperties extends AbstractPropertySpec with SignumLaws {
  property("sum of Ints is lawful according to signum laws") {
    check(forAll { (x: Int) =>
      law[Int](x)
    })
  }

  property("sum of Longs is lawful according to signum laws") {
    check(forAll { (x: Long) =>
      law[Long](x)
    })
  }
}
