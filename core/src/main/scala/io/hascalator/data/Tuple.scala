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

import scala.inline

/** The tuple data types, and associated functions.
  * @author Carlo Micieli
  * @since 0.0.1
  */
object Tuple {
  /** Extract the first component of a pair.
    * @param p a pair
    * @return the first component
    */
  @inline def fst[A, B](p: (A, B)): A = p._1

  /** Extract the second component of a pair.
    * @param p a pair
    * @return the second component
    */
  @inline def snd[A, B](p: (A, B)): B = p._2

  /** Converts an uncurried function to a curried function
    * @param f the function to curry
    * @tparam A the first argument type
    * @tparam B the second argument type
    * @tparam C the return type
    * @return
    */
  def curry[A, B, C](f: ((A, B)) => C): A => (B => C) = { a => b => f((a, b)) }

  /** Converts a curried function to a function on pairs.
    * @param f the function to uncurry
    * @tparam A the first argument type
    * @tparam B the second argument type
    * @tparam C the return type
    * @return
    */
  def uncurry[A, B, C](f: A => (B => C)): ((A, B)) => C = { case (a, b) => f(a)(b) }

  /** Swap the components of a pair.
    * @param p a pair
    * @tparam A the first element type
    * @tparam B the second element type
    * @return a pair, with the element swapped
    */
  @inline def swap[A, B](p: (A, B)): (B, A) = (p._2, p._1)
}
