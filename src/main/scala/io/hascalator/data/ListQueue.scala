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
private[data] case class ListQueue[+A](front: List[A], rear: List[A]) extends Queue[A] {

  def this() = {
    this(List.empty[A], List.empty[A])
  }

  override def enqueue[A1 >: A](el: A1): Queue[A1] = {
    ListQueue(front, el +: rear)
  }

  override def peek: Maybe[A] = {
    if (isEmpty) {
      Maybe.none
    } else {
      front match {
        case x +: _ => Maybe.just(x)
        case _      => rear.reverse.headMaybe
      }
    }
  }

  override def dequeue: Either[EmptyQueueException, (A, Queue[A])] = {
    if (isEmpty) {
      Either.left(new EmptyQueueException with NoStackTrace)
    } else {
      front match {
        case x +: xs => Either.right((x, ListQueue(xs, rear)))
        case _ =>
          val newFront = rear.reverse
          Either.right((newFront.head, ListQueue(newFront.tail, List.empty[A])))
      }
    }
  }

  override def size: Int = {
    front.length + rear.length
  }

  override def isEmpty: Boolean = {
    front.isEmpty && rear.isEmpty
  }

  override def toString: String = {
    val topEl = peek.map(x => s"top = $x").getOrElse("")
    s"Queue($topEl)"
  }
}

object ListQueue {
  def empty[A]: Queue[A] = {
    new ListQueue(List.empty[A], List.empty[A])
  }
}