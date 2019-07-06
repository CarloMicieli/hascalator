import sbt._

object Version {
  final val ScalaTest      = "3.0.8"
  final val ScalaCheck     = "1.14.0"
}

object Library {
  val scalaTest: ModuleID      = "org.scalatest"  %% "scalatest" % Version.ScalaTest
  val scalaCheck: ModuleID     = "org.scalacheck" %% "scalacheck" % Version.ScalaCheck
}

object Scalac {
  final val `2.11.8`: String = "2.11.8"
  final val `2.12.8`: String = "2.12.8"
  final val `2.13.0`: String = "2.13.0"
}
