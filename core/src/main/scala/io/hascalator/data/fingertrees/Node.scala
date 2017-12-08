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

/** An intermediate node in a 2-3 tree, parameterized
  * based on the type of its child.
  *
  * @tparam A element type
  * @author Carlo Micieli
  * @since 0.1
  */
private[fingertrees] sealed trait Node[+V, +A] extends Any {
  def toList: List[A] = {
    this match {
      case Branch2(_, x, y)    => List(x, y)
      case Branch3(_, x, y, z) => List(x, y, z)
    }
  }
}

private[fingertrees] object Node {

  def apply[V, A](x: A, y: A)(implicit mva: Measured[V, A]): Node[V, A] = {
    import mva._
    val v = mappend(measure(x), measure(y))
    Branch2(v, x, y)
  }

  def apply[V, A](x: A, y: A, z: A)(implicit mva: Measured[V, A]): Node[V, A] = {
    import mva._
    val v = mappend(mappend(measure(x), measure(y)), measure(z))
    Branch3(v, x, y, z)
  }

  def fromList[V, A](xs: List[A])(implicit mva: Measured[V, A]): Node[V, A] = {
    xs match {
      case List(x, y)    => apply(x, y)
      case List(x, y, z) => apply(x, y, z)
      case _             => error("Node must contain two or three elements")
    }
  }

  implicit def toMeasured[A, V](implicit mva: Measured[V, A]): Measured[V, Node[V, A]] = new Measured[V, Node[V, A]] {
    override def measure(node: Node[V, A]): V = {
      node match {
        case Branch2(v, _, _)    => v
        case Branch3(v, _, _, _) => v
      }
    }
    override def mempty: V = mva.mempty
    override def mappend(x: V, y: V): V = mva.mappend(x, y)
  }
}

// A node that can have 3 children
private[fingertrees] final case class Branch3[V, A](v: V, x: A, y: A, z: A) extends Node[V, A]

// A node that can have 2 children
private[fingertrees] final case class Branch2[V, A](v: V, x: A, y: A) extends Node[V, A]
