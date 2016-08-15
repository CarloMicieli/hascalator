import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import de.heikoseeberger.sbtheader.HeaderPlugin
import de.heikoseeberger.sbtheader.license._

name := "hascalator"

organization := "io.hascalator"

organizationName := "CarloMicieli"

organizationHomepage := Some(url("http://CarloMicieli.github.io"))

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.8"

homepage := Some(url("https://github.com/CarloMicieli/hascalator"))

scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-encoding", "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-unused-import",
  "-Ywarn-numeric-widen",
  "-Ywarn-infer-any",
  "-Ywarn-value-discard",
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code",
  "-J-Xss6M"
)

scalacOptions in (Compile, console) --= Seq(
  "-Xfatal-warnings",
  "-Ywarn-dead-code",
  "-Ywarn-unused-import"
)

libraryDependencies ++= Seq(
  Library.scalaLogging,
  Library.typesafeConfig,
  Library.logback,
  Library.scalaCheck % "test",
  Library.scalaTest % "test"
)

lazy val scalaProject = (project in file("."))
  .enablePlugins(SbtScalariform)
  .enablePlugins(AutomateHeaderPlugin)
  .enablePlugins(GitVersioning)
  .enablePlugins(GitBranchPrompt)

initialCommands := """|import io.hascalator._
                      |import io.hascalator.data._
                      |import io.hascalator.math._
                      |import io.hascalator.functions._
                      |""".stripMargin

// Header settings
HeaderPlugin.autoImport.headers := Map("scala" -> Apache2_0("2016", "Carlo Micieli"))

// Scalariform settings
SbtScalariform.scalariformSettings
ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)
  .setPreference(PreserveDanglingCloseParenthesis, true)

// tut
tutSettings

fork in run := true
