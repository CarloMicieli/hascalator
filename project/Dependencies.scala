import sbt._

object Version {
  final val ScalaTest      = "3.0.1"
  final val ScalaCheck     = "1.13.4"
  final val ScalaLogging   = "3.5.0"
  final val TypesafeConfig = "1.3.1"
  final val Logback        = "1.1.7"

}

object Library {
  val logback: ModuleID        = "ch.qos.logback" %  "logback-classic" % Version.Logback
  val scalaLogging: ModuleID   = "com.typesafe.scala-logging" %% "scala-logging" % Version.ScalaLogging
  val typesafeConfig: ModuleID = "com.typesafe"   % "config" % Version.TypesafeConfig
  val scalaTest: ModuleID      = "org.scalatest"  %% "scalatest" % Version.ScalaTest
  val scalaCheck: ModuleID     = "org.scalacheck" %% "scalacheck" % Version.ScalaCheck
}

object scalac {
  final val `2.11.8`: String = "2.11.8"
}