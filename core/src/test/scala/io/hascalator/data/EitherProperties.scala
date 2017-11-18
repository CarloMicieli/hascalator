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

import tests.arbitrary.either._
import io.hascalator.AbstractPropertySpec
import org.scalacheck.Prop.forAll

class EitherProperties extends AbstractPropertySpec {
  property("map: identity law") {
    check(forAll { (e: Either[String, Int]) =>
      e.map(id) === e
    })
  }

  property("map: composition law") {
    check(forAll { (e: Either[String, Int]) =>
      val double: Int => Int = _ * 2
      val addOne: Int => Int = _ + 1

      e.map(double).map(addOne) === e.map(double andThen addOne)
    })
  }

  property("exists: return true for Right values") {
    check(forAll { (e: Either[String, Int]) =>
      val result = e.exists(_ > 0)
      e.isLeft || (e.isRight && result == (e.get > 0))
    })
  }

  property("swap: change left to right values or viceversa") {
    check(forAll { (e: Either[String, Int]) =>
      e.swap.isLeft === e.isRight
      e.swap.isRight === e.isLeft
    })
  }

  property("toMaybe: translates Right to Just") {
    check(forAll { (e: Either[String, Int]) =>
      val e2 = e.toMaybe
      e2.isEmpty === e.isLeft
    })
  }

  property("zip: produces right values when both are right") {
    check(forAll { (e1: Either[String, Int], e2: Either[String, Int]) =>
      (e1 zip e2).isRight === (e1.isRight && e2.isRight)
    })
  }

  property("zip: produces left values when either one of the valus is a left") {
    check(forAll { (e1: Either[String, Int], e2: Either[String, Int]) =>
      (e1 zip e2).isLeft === (e1.isLeft || e2.isLeft)
    })
  }
}
