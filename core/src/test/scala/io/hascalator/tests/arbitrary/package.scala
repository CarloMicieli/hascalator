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
package tests

/** @author Carlo Micieli
  * @since 0.0.1
  */
package object arbitrary {

  object all extends ArbitraryList
    with ArbitraryMaybe
    with ArbitraryEither
    with ArbitraryStack
    with ArbitraryStackOp
    with ArbitraryRatio
    with ArbitraryBankersDequeue
    with ArbitrarySet

  object list extends ArbitraryList
  object maybe extends ArbitraryMaybe
  object either extends ArbitraryEither
  object stack extends ArbitraryStack
  object bankersDequeue extends ArbitraryBankersDequeue
  object stackOp extends ArbitraryStackOp
  object ratio extends ArbitraryRatio
  object set extends ArbitrarySet
}
