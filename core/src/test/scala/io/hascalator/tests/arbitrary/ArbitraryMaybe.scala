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
package tests.arbitrary

import io.hascalator.data.Maybe
import org.scalacheck.{ Arbitrary, Gen }

/** @author Carlo Micieli
  * @since 0.0.1
  */
trait ArbitraryMaybe {
  implicit def arbitraryMaybe[T](implicit a: Arbitrary[T]): Arbitrary[Maybe[T]] = Arbitrary {
    val genNone: Gen[Maybe[T]] = {
      Gen.const(Maybe.none[T])
    }

    def genJust: Gen[Maybe[T]] = {
      for (x <- a.arbitrary) yield Maybe.just(x)
    }

    Gen.oneOf(genNone, genJust)
  }
}
