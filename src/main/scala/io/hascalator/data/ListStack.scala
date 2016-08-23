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
import scala.util.control.NoStackTrace
import scala.StringContext

/**
  * @author Carlo Micieli
  * @since 0.0.1
  */
private[this] class ListStack[+A](st: List[A]) extends Stack[A] {
  override def push[A1 >: A](el: A1): Stack[A1] = {
    new ListStack[A1](el +: st)
  }

  override def size: Int = st.length

  override def top: Maybe[A] = st.headMaybe

  override def isEmpty: Boolean = st.isEmpty

  override def nonEmpty: Boolean = st.nonEmpty

  override def popUntil(p: (A) => Boolean): (List[A], Stack[A]) = {
    val (removed, remaining) = st.span(p)
    (removed, new ListStack(remaining))
  }

  override def pop: Either[EmptyStackException, (A, Stack[A])] =
    if (isEmpty) {
      Either.left(new EmptyStackException with NoStackTrace)
    } else {
      val head +: tail = st
      Either.right((head, new ListStack(tail)))
    }

  override def foreach[U](f: (A) => U): Unit = st.foreach(f)

  override def toString: String = {
    if (isEmpty) {
      "<emptystack>"
    } else {
      s"<stack:top = ${top.get}>"
    }
  }
}
