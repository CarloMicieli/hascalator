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
package typeclasses

import Prelude._

import io.hascalator.AbstractPropertySpec
import org.scalacheck.Prop.forAll

class EnumProperties extends AbstractPropertySpec {
  property("Boolean is made instance of Enum typeclass") {
    check(forAll { (b: Boolean) =>
      val enum = implicitly[Enum[Boolean]]
      enum.toEnum(enum.fromEnum(b)).isDefined
    })
  }

  property("Char is made instance of Enum typeclass") {
    check(forAll { (b: Char) =>
      val enum = implicitly[Enum[Char]]
      enum.toEnum(enum.fromEnum(b)).isDefined
    })
  }

  property("Int is made instance of Enum typeclass") {
    check(forAll { (b: Int) =>
      val enum = implicitly[Enum[Int]]
      enum.toEnum(enum.fromEnum(b)).isDefined
    })
  }

  property("Float is made instance of Enum typeclass") {
    check(forAll { (b: Float) =>
      val enum = implicitly[Enum[Float]]
      enum.toEnum(enum.fromEnum(b)).isDefined
    })
  }

  property("Double is made instance of Enum typeclass") {
    check(forAll { (b: Double) =>
      val enum = implicitly[Enum[Double]]
      enum.toEnum(enum.fromEnum(b)).isDefined
    })
  }
}
