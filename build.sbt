ThisBuild / organization := "com.apollographql"

val scala212 = "2.12.10"
val scala213 = "2.13.1"

ThisBuild / crossScalaVersions := Seq(scala212, scala213)
ThisBuild / scalaVersion := scala212

ThisBuild / scalacOptions ++= Seq("-feature", "-deprecation")

ThisBuild / licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

ThisBuild / bintrayOrganization := Some("apollographql")

ThisBuild / bintrayReleaseOnPublish := false

lazy val root = project.in(file(".")).withId("apollo-scalajs").aggregate(
  core, react
).settings(
  publish := {},
  publishLocal := {}
)

lazy val macroAnnotationSettings = Seq(
  resolvers += Resolver.sonatypeRepo("releases"),
  scalacOptions ++= {
    if (scalaVersion.value == scala213) Seq("-Ymacro-annotations")
    else Seq.empty
  },
  libraryDependencies ++= {
    if (scalaVersion.value == scala213) Seq.empty
    else Seq(compilerPlugin(("org.scalamacros" % "paradise" % "2.1.1").cross(CrossVersion.full)))
  }
)

lazy val core = project.enablePlugins(BintrayPlugin)

lazy val react = project.dependsOn(core).settings(macroAnnotationSettings).enablePlugins(BintrayPlugin)

lazy val tests = project.dependsOn(core, react).settings(macroAnnotationSettings)

lazy val example = project.dependsOn(core, react).settings(macroAnnotationSettings)
