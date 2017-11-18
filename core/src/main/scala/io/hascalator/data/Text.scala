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

/** The ''Text'' types and associated operations
  * @author Carlo Micieli
  * @since 0.0.1
  */
object Text {
  type Text = String

  /** A `Word` is a list of characters.
    */
  type Word = List[Char]

  /** A `Word` is a list of words.
    */
  type Line = List[Word]

  /** Breaks a string up into a list of strings at newline characters.
    * The resulting strings do not contain newlines.
    *
    * @param s
    * @return
    */
  def lines(s: Text): List[Line] = undefined

  /** Breaks a string up into a list of words, which were delimited by white space.
    * @param s
    * @return
    */
  def words(s: Text): List[Word] = undefined

  /** Is an inverse operation to [[lines]]. It joins lines, after appending a terminating newline to each.
    * @param lines
    * @return
    */
  def unLines(lines: List[Line]): Text = undefined

  /** Is an inverse operation to [[words]]. It joins words with separating spaces.
    * @param words
    * @return
    */
  def unWords(words: List[Word]): Text = undefined
}
