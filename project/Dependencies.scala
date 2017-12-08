import sbt._

object Version {
  final val ScalaTest  = "3.0.4"
  final val ScalaCheck = "1.13.4"
}

object Library {
  val scalaTest: ModuleID  = "org.scalatest"  %% "scalatest" % Version.ScalaTest
  val scalaCheck: ModuleID = "org.scalacheck" %% "scalacheck" % Version.ScalaCheck
  val simulacrum: ModuleID = "com.github.mpilquist" %% "simulacrum" % "0.11.0"
}

object Scalac {
  final val `2.12.4`: String = "2.12.4"
}
