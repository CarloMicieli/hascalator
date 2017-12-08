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

/** Monoidal size – all leaves have Size 1.
  * @param getSize
  */
private[fingertrees] final case class Size(getSize: Int) extends AnyVal

private[fingertrees] object Size {

  implicit val sizeMonoid: Monoid[Size] = new Monoid[Size] {
    override def mempty: Size = Size(0)
    override def mappend(x: Size, y: Size): Size = Size(x.getSize + y.getSize)
  }
}