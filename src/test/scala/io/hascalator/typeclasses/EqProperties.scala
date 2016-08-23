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

class EqProperties extends AbstractPropertySpec with EqLaws {
  property("Byte respect the Eq type class laws") {
    check(forAll { (x: Byte, y: Byte, z: Byte) =>
      checkAllLaws(x, y, z)(n => (n % 2).toByte)
    })
  }

  property("Short respect the Eq type class laws") {
    check(forAll { (x: Short, y: Short, z: Short) =>
      checkAllLaws(x, y, z)(n => (n % 2).toShort)
    })
  }

  property("Int respect the Eq type class laws") {
    check(forAll { (x: Int, y: Int, z: Int) =>
      checkAllLaws(x, y, z)(_ % 2)
    })
  }

  property("Char respect the Eq type class laws") {
    check(forAll { (x: Char, y: Char, z: Char) =>
      checkAllLaws(x, y, z)(ch => (ch.toInt + 1).toChar)
    })
  }

  property("String respect the Eq type class laws") {
    check(forAll { (x: String, y: String, z: String) =>
      checkAllLaws(x, y, z)(_ * 2)
    })
  }

  property("Float respect the Eq type class laws") {
    check(forAll { (x: Float, y: Float, z: Float) =>
      checkAllLaws(x, y, z)(_ * 2)
    })
  }

  property("Double respect the Eq type class laws") {
    check(forAll { (x: Double, y: Double, z: Double) =>
      checkAllLaws(x, y, z)(_ * 2)
    })
  }

  property("Boolean respect the Eq type class laws") {
    check(forAll { (x: Boolean, y: Boolean, z: Boolean) =>
      checkAllLaws(x, y, z)(id)
    })
  }

  def checkAllLaws[A: Eq](x: A, y: A, z: A)(f: A => A): Boolean = {
    reflexivityLaw(x)
    symmetryLaw(x, y)
    transitivityLaw(x, y, z)
    substitutionLaw(x, x)(f)
  }
}
