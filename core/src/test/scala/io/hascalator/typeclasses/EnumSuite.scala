/*
 * Copyright 2016 CarloMicieli
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
import io.hascalator.AbstractTestSuite
import scala.Stream

class EnumSuite extends AbstractTestSuite with ColorsEnum {
  "prev" should "return the previous element if exists" in {
    prev(Red) shouldBe Maybe.just(Black)
  }

  "prev" should "return None when the value doesn't exist" in {
    prev(Black) shouldBe Maybe.none
  }

  "succ" should "return the next element" in {
    succ(Red) shouldBe Maybe.just(Yellow)
  }

  "succ" should "return None when the value doesn't exist" in {
    succ(Cyan) shouldBe Maybe.none
  }

  "enumFrom" should "produce a Stream with the enum values" in {
    enumFrom(Yellow).toList.mkString(", ") shouldBe "Yellow, Blue, Green, Orange, Pink, Cyan"
  }

  "enumFromThen" should "take in consideration the step value" in {
    enumFromThen(Black, Yellow).toList.mkString(", ") shouldBe "Black, Yellow, Green, Pink"
  }

  "enumFromThen" should "return the empty Stream when x > y" in {
    enumFromThen(Yellow, Black) shouldBe Stream.empty[Color]
  }

  "enumFromTo" should "build a list from start to end" in {
    enumFromTo(Black, Yellow) shouldBe List(Black, Red, Yellow)
  }

  "enumFromTo" should "return the empty list when start > end" in {
    enumFromTo(Yellow, Black) shouldBe List.empty[Color]
  }

  "enumFromThenTo" should "take in consideration the step value" in {
    enumFromThenTo(Black, Yellow, Cyan) shouldBe List(Black, Yellow, Green, Pink)
  }

  "enumFromThenTo" should "return the empty list when x > y" in {
    enumFromThenTo(Yellow, Black, Cyan) shouldBe List.empty[Color]
  }

  "enumFromThenTo" should "return a list with only one element if y > bound" in {
    enumFromThenTo(Black, Yellow, Red) shouldBe List(Black)
  }
}

sealed trait Color {
  def id: Int
}
case object Black extends Color { override val id: Int = 0 }
case object Red extends Color { override val id: Int = 1 }
case object Yellow extends Color { override val id: Int = 2 }
case object Blue extends Color { override val id: Int = 3 }
case object Green extends Color { override val id: Int = 4 }
case object Orange extends Color { override val id: Int = 5 }
case object Pink extends Color { override val id: Int = 6 }
case object Cyan extends Color { override val id: Int = 7 }

trait ColorsEnum extends Enum[Color] {
  private val colors = List(Black, Red, Yellow, Blue, Green, Orange, Pink, Cyan)

  override def fromEnum(x: Color): Int = x.id
  override def toEnum(x: Int): Maybe[Color] = {
    colors.find(_.id == x)
  }
}