import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import de.heikoseeberger.sbtheader.HeaderPlugin
import de.heikoseeberger.sbtheader.license._
import scoverage.ScoverageKeys
import com.typesafe.sbt.SbtSite.SiteKeys._
import com.typesafe.sbt.SbtGhPages.GhPagesKeys._
import sbtunidoc.Plugin.UnidocKeys._

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

lazy val additionalScalacOptions = Set(
  "-Yno-imports",
  "-Yno-predef",
  "-Xfatal-warnings",
  "-Ywarn-dead-code",
  "-Ywarn-unused-import"
)

lazy val core = (project in file("core")).
  settings(commonSettings).
  settings(scalacOptions ++= commonScalacOptions ++ additionalScalacOptions).
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
                                 |import Prelude._
                                 |""".stripMargin)

lazy val bench = (project in file("bench")).
  settings(commonSettings).
  settings(scalacOptions ++= commonScalacOptions).
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

lazy val docs = (project in file("docs")).
  settings(commonSettings).
  settings(scalacOptions ++= commonScalacOptions).
  dependsOn(core).
  settings(noPublishSettings).
  settings(
    site.settings,
    site.includeScaladoc(),
    tutSettings,
    ghpages.settings,
    site.addMappingsToSiteDir(tut, "tut"),
    ghpagesNoJekyll := false,
    includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.yml" | "*.md",
    git.remoteRepo := "git@github.com:CarloMicieli/hascalator.git"
  )

lazy val scalaProject = (project in file("."))
  .settings(moduleName := "root")
  .enablePlugins(GitVersioning)
  .enablePlugins(GitBranchPrompt)
  .settings(noPublishSettings)
  .aggregate(core, docs, bench)

fork in run := true

addCommandAlias("run-benchmarks", ";bench/jmh:run -i 10 -wi 10 -f1 -t1")

addCommandAlias("build", ";core/compile ;core/test ;bench/test")
addCommandAlias("validate", ";scalastyle ;build ;makeSite")
