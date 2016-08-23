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

import Prelude._

/**
  * @author Carlo Micieli
  * @since 0.0.1
  */
private[this] case class SizedList[+A](xs: List[A], size: Int) {
  def this() = this(List.empty[A], 0)

  def isEmpty: Boolean = size == 0

  def head: A = xs.head

  def headOption: Maybe[A] = xs.headMaybe

  def tail: SizedList[A] = {
    if (isEmpty) {
      this
    } else {
      SizedList(xs.tail, size - 1)
    }
  }

  def +:[A1 >: A](x: A1): SizedList[A1] = {
    SizedList(x +: xs, size + 1)
  }

  def union[A1 >: A](that: SizedList[A1]): SizedList[A1] = {
    (this, that) match {
      case (SizedList(Nil, _), sl2) => sl2
      case (sl1, SizedList(Nil, _)) => sl1
      case (SizedList(ys, len1), SizedList(zs, len2)) =>
        SizedList(ys ++ zs, len1 + len2)
    }
  }

  def reverse: SizedList[A] = {
    SizedList(xs.reverse, size)
  }
}

private[data] object SizedList {
  def empty[A]: SizedList[A] = new SizedList[A]
}