import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import de.heikoseeberger.sbtheader.HeaderPlugin
import de.heikoseeberger.sbtheader.license._
import scoverage.ScoverageKeys

name := "hascalator"

lazy val scoverageSettings = Seq(
  ScoverageKeys.coverageMinimum       := 60,
  ScoverageKeys.coverageFailOnMinimum := false,
  ScoverageKeys.coverageHighlighting  := true,
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

lazy val scalariformPluginSettings = Seq(
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)
    .setPreference(MultilineScaladocCommentsStartOnFirstLine, true)
    .setPreference(DanglingCloseParenthesis, Preserve)
)

lazy val commonSettings = Seq(
  organization := "io.hascalator",
  organizationName := "CarloMicieli",
  organizationHomepage := Some(url("http://CarloMicieli.github.io")),
  scalaVersion := Scalac.`2.12.2`,
  homepage := Some(url("https://github.com/CarloMicieli/hascalator")),
  licenses := Seq(("Apache License, Version 2.0", url("http://www.apache.org/licenses/LICENSE-2.0")))
)

lazy val core = (project in file("core"))
  .settings(commonSettings)
  .settings(
    scalacOptions ++= ScalacOptions.Default,
    scalacOptions in (Compile, console) ~= ScalacOptions.ConsoleDefault,
    scalacOptions in Test ~= ScalacOptions.TestDefault)
  .settings(automateHeaderPluginSettings: _*)
  .settings(scoverageSettings: _*)
  .settings(SbtScalariform.scalariformSettings)
  .settings(scalariformPluginSettings: _*)
  .settings(addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4"))
  .enablePlugins(SbtScalariform)
  .enablePlugins(AutomateHeaderPlugin)
  .settings(libraryDependencies ++= Seq(
    Library.scalaCheck % "test",
    Library.scalaTest % "test"
  ))

/*
lazy val bench = (project in file("bench"))
  .settings(commonSettings)
  .settings(scalacOptions ++= ScalacOptions.BenchmarkDefault)
  .settings(scoverageSettings: _*)
  .settings(automateHeaderPluginSettings: _*)
  .settings(SbtScalariform.scalariformSettings)
  .settings(scalariformPluginSettings: _*)
  enablePlugins(SbtScalariform)
  enablePlugins(AutomateHeaderPlugin)
  enablePlugins(JmhPlugin)
  dependsOn(core)
*/

lazy val docs = (project in file("docs"))
  .settings(commonSettings)
  .settings(moduleName := "hascalator-docs")
  .settings(scalacOptions ++= ScalacOptions.SiteDefault)
  .settings(noPublishSettings)
  .enablePlugins(ScalaUnidocPlugin)
  .enablePlugins(SiteScaladocPlugin)
  .aggregate(core)
  .dependsOn(core)

lazy val scalaProject = (project in file("."))
  .settings(moduleName := "root")
  .settings(commonSettings)
  .enablePlugins(GitVersioning)
  .enablePlugins(GitBranchPrompt)
  .settings(noPublishSettings)
  .dependsOn(core, docs)
  .aggregate(core)
  .settings(initialCommands := """|import io.hascalator._
                                  |import Prelude._
                                  |""".stripMargin)

fork in run := true

addCommandAlias("run-benchmarks", ";bench/jmh:run -i 10 -wi 10 -f1 -t1")

addCommandAlias("build", ";core/compile ;core/test ;bench/test")
addCommandAlias("validate", ";scalastyle ;build ;makeSite")
