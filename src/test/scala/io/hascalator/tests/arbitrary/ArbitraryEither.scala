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
package tests.arbitrary

import io.hascalator.data.Either
import org.scalacheck.{ Arbitrary, Gen }

/** @author Carlo Micieli
  * @since 0.0.1
  */
trait ArbitraryEither {
  implicit def arbitraryEither[A, B](implicit a: Arbitrary[A], b: Arbitrary[B]): Arbitrary[Either[A, B]] = Arbitrary {
    def genLeft: Gen[Either[A, B]] = {
      for (x <- a.arbitrary) yield Either.left(x)
    }

    def genRight: Gen[Either[A, B]] = {
      for (x <- b.arbitrary) yield Either.right(x)
    }

    Gen.oneOf(genLeft, genRight)
  }
}
