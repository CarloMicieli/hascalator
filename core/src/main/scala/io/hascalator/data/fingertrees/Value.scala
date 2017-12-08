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
package fingertrees

import Prelude._

private[fingertrees] final case class Value[A](value: A) extends AnyVal

private[fingertrees] object Value {
  implicit def toMeasuredValue[A]: Measured[Size, Value[A]] = new Measured[Size, Value[A]] {
    private val monoidSize = Monoid[Size]

    //All values just have size one.
    override def measure(a: Value[A]): Size = Size(1)

    override def mempty: Size = monoidSize.mempty
    override def mappend(x: Size, y: Size): Size = monoidSize.mappend(x, y)
  }
}