import sbt._

object Version {
  final val ScalaTest      = "3.0.4"
  final val ScalaCheck     = "1.13.4"
}

object Library {
  val scalaTest: ModuleID      = "org.scalatest"  %% "scalatest" % Version.ScalaTest
  val scalaCheck: ModuleID     = "org.scalacheck" %% "scalacheck" % Version.ScalaCheck
}

object Scalac {
  final val `2.11.8`: String = "2.11.8"
  final val `2.12.3`: String = "2.12.3"
  final val `2.12.4`: String = "2.12.4"
}
