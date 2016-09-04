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
package tests.generator

import Prelude._
import org.scalacheck.{ Arbitrary, Gen }
import tests.arbitrary.list._
import scala.math.Numeric

/** @author Carlo Micieli
  */
trait ListGenerators {
  import Gen._

  def nonEmptyList[A](implicit a: Arbitrary[A]): Gen[List[A]] = {
    nonEmptyContainerOf[List, A](a.arbitrary)
  }

  def nonEmptyNegativeList[A](implicit n: Numeric[A], c: Choose[A]): Gen[List[A]] = {
    nonEmptyContainerOf[List, A](negNum[A])
  }

  def nonEmptyPositiveList[A](implicit n: Numeric[A], c: Choose[A]): Gen[List[A]] = {
    nonEmptyContainerOf[List, A](posNum[A])
  }
}
