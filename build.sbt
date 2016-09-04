import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import de.heikoseeberger.sbtheader.HeaderPlugin
import de.heikoseeberger.sbtheader.license._
import scoverage.ScoverageKeys

name := "hascalator"

lazy val scoverageSettings = Seq(
  ScoverageKeys.coverageMinimum := 60,
  ScoverageKeys.coverageFailOnMinimum := false,
  ScoverageKeys.coverageHighlighting := true,
  ScoverageKeys.coverageExcludedPackages := "io\\.hascalator\\.benchmarks\\..*"
)

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

// Header settings
lazy val automateHeaderPluginSettings = Seq(
  HeaderPlugin.autoImport.headers := Map("scala" -> Apache2_0("2016", "Carlo Micieli"))
)

lazy val commonSettings = Seq(
  organization := "io.hascalator",
  organizationName := "CarloMicieli",
  organizationHomepage := Some(url("http://CarloMicieli.github.io")),
  scalaVersion := "2.11.8",
  homepage := Some(url("https://github.com/CarloMicieli/hascalator"))
)

lazy val commonScalacOptions = Seq(
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

scalacOptions in (Compile, console) ~= (_.filterNot(Set(
  "-Yno-imports",
  "-Xfatal-warnings",
  "-Ywarn-dead-code",
  "-Ywarn-unused-import"
)))

scalacOptions in Test ~= (_.filterNot(Set(
  "-Ywarn-unused-import",
  "-Yno-imports",
  "-Yno-predef"
)))

lazy val core = (project in file("core")).
  settings(commonSettings).
  settings(scalacOptions ++= commonScalacOptions).
  settings(scalacOptions in (Compile, console) ~= (_.filterNot(Set(
      "-Yno-imports",
      "-Xfatal-warnings",
      "-Ywarn-dead-code",
      "-Ywarn-unused-import"
    ))),
    scalacOptions in Test ~= (_.filterNot(Set(
      "-Ywarn-unused-import",
      "-Yno-imports",
      "-Yno-predef"
    )))).
  settings(automateHeaderPluginSettings: _*).
  settings(scoverageSettings: _*).
  settings(
    SbtScalariform.scalariformSettings,
    ScalariformKeys.preferences := ScalariformKeys.preferences.value
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(DoubleIndentClassDeclaration, true)
      .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)
      .setPreference(MultilineScaladocCommentsStartOnFirstLine, true)
      .setPreference(PreserveDanglingCloseParenthesis, true)).
  enablePlugins(SbtScalariform).
  enablePlugins(AutomateHeaderPlugin).
  settings(libraryDependencies ++= Seq(
    Library.scalaLogging,
    Library.typesafeConfig,
    Library.logback,
    Library.scalaCheck % "test",
    Library.scalaTest % "test"
  )).
  settings(initialCommands := """|import io.hascalator._
                                 |""".stripMargin)

lazy val bench = (project in file("bench")).
  settings(commonSettings).
  settings(scalacOptions ++= commonScalacOptions).
  settings(scalacOptions in (Compile, console) ~= (_.filterNot(Set(
      "-Yno-imports",
      "-Yno-predef",
      "-Xfatal-warnings",
      "-Ywarn-dead-code",
      "-Ywarn-unused-import"
    )))).
  settings(scoverageSettings: _*).
  settings(automateHeaderPluginSettings: _*).
  settings(
    SbtScalariform.scalariformSettings,
    ScalariformKeys.preferences := ScalariformKeys.preferences.value
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(DoubleIndentClassDeclaration, true)
      .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)
      .setPreference(MultilineScaladocCommentsStartOnFirstLine, true)
      .setPreference(PreserveDanglingCloseParenthesis, true)).
  enablePlugins(SbtScalariform).
  enablePlugins(AutomateHeaderPlugin).
  enablePlugins(JmhPlugin).
  dependsOn(core)

lazy val docs = project.
  settings(noPublishSettings).
  dependsOn(core)

lazy val scalaProject = (project in file("."))
  .settings(moduleName := "root")
  .enablePlugins(GitVersioning)
  .enablePlugins(GitBranchPrompt)
  .aggregate(core)

fork in run := true

addCommandAlias("run-benchmarks", ";bench/jmh:run -i 10 -wi 10 -f1 -t1")
