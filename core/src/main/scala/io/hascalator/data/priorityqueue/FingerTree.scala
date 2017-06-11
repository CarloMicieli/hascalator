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
package priorityqueue

//import Prelude._

/** A 2-3 tree, a functional representation of persistent sequences supporting access to the ends in amortized constant
  * time, and concatenation and splitting in time logarithmic in the size of the smaller piece
  *
  * @author Carlo Micieli
  * @since 0.0.1
  */
sealed trait FingerTree[A] {
  sealed trait Tree
  case class Zero(x: A) extends Tree
  case class Succ(t: Tree, n: Node) extends Tree

  sealed trait Node
  case class Node2(x: A, y: A) extends Node
  case class Node3(x: A, y: A, z: A) extends Node
}

