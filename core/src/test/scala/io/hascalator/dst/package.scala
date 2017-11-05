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

import org.scalatest.enablers.Size
import Prelude._

package object dst {
  implicit def treeSize[K, V]: Size[Tree[K, V]] = new Size[Tree[K, V]] {
    override def sizeOf(obj: Tree[K, V]): Long = obj.size.toLong
  }
}
