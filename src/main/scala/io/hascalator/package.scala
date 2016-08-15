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

package io

package object hascalator {
  type Eq[A] = io.hascalator.typeclasses.Eq[A]
  val Eq = io.hascalator.typeclasses.Eq

  type Ord[A] = io.hascalator.typeclasses.Ord[A]
  val Ord = io.hascalator.typeclasses.Ord

  type Show[A] = io.hascalator.typeclasses.Show[A]
  val Show = io.hascalator.typeclasses.Show

  type Num[A] = io.hascalator.typeclasses.Num[A]
  val Num = io.hascalator.typeclasses.Num

  type Integral[A] = io.hascalator.typeclasses.Integral[A]
  val Integral = io.hascalator.typeclasses.Integral

  type Fractional[A] = io.hascalator.typeclasses.Fractional[A]
  val Fractional = io.hascalator.typeclasses.Fractional
}
