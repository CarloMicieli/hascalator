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

class FractionalProperties extends AbstractPropertySpec {
  property("Float: is an instance of the Fractional typeclass") {
    check(forAll { (x: Float, y: Float) =>
      val fr = Fractional[Float]
      fr.div(x, y) === x / y
      fr.recip(x) === 1.0f / x
    })
  }

  property("Double: is an instance of the Fractional typeclass") {
    check(forAll { (x: Double, y: Double) =>
      val fr = Fractional[Double]
      fr.div(x, y) === x / y
      fr.recip(x) === 1.0d / x
    })
  }
}
