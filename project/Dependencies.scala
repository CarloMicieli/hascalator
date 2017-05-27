import sbt._

object Version {
  final val Scala          = "2.11.8"

  final val ScalaTest      = "3.0.1"
  final val ScalaCheck     = "1.13.4"
  final val ScalaLogging   = "3.5.0"
  final val TypesafeConfig = "1.3.1"
  final val Logback        = "1.1.7"
}

object Library {
  val logback        = "ch.qos.logback" %  "logback-classic" % Version.Logback
  val scalaLogging   = "com.typesafe.scala-logging" %% "scala-logging" % Version.ScalaLogging

  val typesafeConfig = "com.typesafe"   % "config" % Version.TypesafeConfig

  val scalaTest      = "org.scalatest"  %% "scalatest" % Version.ScalaTest
  val scalaCheck     = "org.scalacheck" %% "scalacheck" % Version.ScalaCheck
}
