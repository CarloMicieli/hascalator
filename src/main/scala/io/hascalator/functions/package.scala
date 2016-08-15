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

import scala.util.control.NoStackTrace

package object functions {

  /**
    * The identity function
    * @tparam A the function type
    * @return the identity function
    */
  def id[A]: A => A = identity

  /**
    * `const(x)` is a unary function which evaluates to `x` for all inputs.
    * @param const the const value the function will always return
    * @tparam A the output type
    * @tparam B the input type
    * @return a constant function
    */
  def const[A, B](const: A): B => A = _ => const

  /**
    * `flip(f)` takes its (first) two arguments in the reverse order of `f`.
    * @param f a binary function
    * @tparam A the first argument type
    * @tparam B the second argument type
    * @tparam C the return type
    * @return a function with the two arguments reversed
    */
  def flip[A, B, C](f: (A, B) => C): (B, A) => C = (b, a) => f(a, b)

  /**
    * `error` stops execution and displays an error message.
    * @param msg the error message
    * @tparam A the return type
    * @return
    */
  def error[A](msg: String): A = {
    throw new ApplicationException(s"*** Exception: '$msg'")
  }

  /**
    * A variant of [[error]] that does not produce a stack trace.
    * @param msg the error message
    * @tparam A the return type
    * @return
    */
  def errorWithoutStackTrace[A](msg: String): A = {
    throw new ApplicationException(s"*** Exception: '$msg'") with NoStackTrace
  }

  /**
    * `until p f` yields the result of applying `f` until `p` holds.
    *
    * @param p the termination condition
    * @param f the function to compute the next value
    * @param z the initial value
    * @tparam A
    * @return the result
    */
  @annotation.tailrec
  final def until[A](p: A => Boolean)(f: A => A)(z: A): A = {
    if (p(z)) {
      z
    } else {
      until(p)(f)(f(z))
    }
  }

  implicit class Function2Flip[A, B, C](val f: (A, B) => C) extends AnyVal {
    def flip: (B, A) => C = functions.flip(f)
  }

  implicit class ForwardPipe[A](val x: A) extends AnyVal {
    def |>[B](f: A => B): B = f(x)
  }

  implicit class ForwardComp[A, B](val f: A => B) extends AnyVal {
    def >>[C](g: B => C): A => C = f andThen g
  }
}

class ApplicationException(msg: String) extends Exception(msg)