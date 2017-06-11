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
package control

import Prelude._
import scala.annotation.implicitNotFound

/** The Monad class defines the basic operations over a monad, a concept from a
  * branch of mathematics known as category theory. From the perspective of a
  * Scala programmer, however, it is best to think of a monad as an abstract datatype
  * of actions.
  *
  * Scala's for comprehension provide a convenient syntax for writing monadic expressions.
  *
  * @tparam M
  * @author Carlo Micieli
  * @since 0.0.1
  */
@implicitNotFound("The type ${M} was not made instance of the Monad type class")
trait Monad[M] extends Any { self =>
}