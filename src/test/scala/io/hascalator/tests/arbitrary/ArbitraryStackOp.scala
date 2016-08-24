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

import io.hascalator.data.{ PopOp, PushOp, StackOp }
import org.scalacheck.{ Arbitrary, Gen }

/** @author Carlo Micieli
  * @since 0.0.1
  */
trait ArbitraryStackOp {
  implicit def arbitraryStackOp[T](implicit a: Arbitrary[T]): Arbitrary[StackOp[T]] = Arbitrary {
    import Arbitrary._
    import Gen._

    val genPopOp = const(PopOp)

    def genPushOp = for { v <- arbitrary[T] } yield PushOp(v)

    frequency((1, genPopOp), (2, genPushOp))
  }
}
