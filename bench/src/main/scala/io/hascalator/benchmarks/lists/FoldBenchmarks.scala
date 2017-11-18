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

package io.hascalator.benchmarks
package lists

import io.hascalator.Prelude._
import org.openjdk.jmh.annotations.{ Benchmark, BenchmarkMode, Mode, OutputTimeUnit }
import java.util.concurrent.TimeUnit

class FoldBenchmarks {
  import BenchmarkState.sample._

  @Benchmark @BenchmarkMode(Array(Mode.Throughput)) @OutputTimeUnit(TimeUnit.SECONDS)
  def foldLeftListBenchmark(): Int = {
    list.foldLeft(0)(_ + _)
  }

  @Benchmark @BenchmarkMode(Array(Mode.Throughput)) @OutputTimeUnit(TimeUnit.SECONDS)
  def foldLeftScalaListBenchmark(): Int = {
    scalaList.foldLeft(0)(_ + _)
  }

  @Benchmark @BenchmarkMode(Array(Mode.Throughput)) @OutputTimeUnit(TimeUnit.SECONDS)
  def foldLeftArrayBenchmark(): Int = {
    array.foldLeft(0)(_ + _)
  }

  @Benchmark @BenchmarkMode(Array(Mode.Throughput)) @OutputTimeUnit(TimeUnit.SECONDS)
  def foldRightListBenchmark(): Int = {
    list.foldRight(0)(_ + _)
  }

  @Benchmark @BenchmarkMode(Array(Mode.Throughput)) @OutputTimeUnit(TimeUnit.SECONDS)
  def foldRightScalaListBenchmark(): Int = {
    scalaList.foldRight(0)(_ + _)
  }

  @Benchmark @BenchmarkMode(Array(Mode.Throughput)) @OutputTimeUnit(TimeUnit.SECONDS)
  def foldRightArrayBenchmark(): Int = {
    array.foldRight(0)(_ + _)
  }
}