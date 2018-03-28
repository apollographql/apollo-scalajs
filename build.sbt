enablePlugins(BintrayPlugin)

organization in ThisBuild := "com.apollographql"

version in ThisBuild := "0.3.0"

scalaVersion in ThisBuild := "2.12.4"

scalacOptions in ThisBuild ++= Seq("-feature", "-deprecation")

licenses in ThisBuild += ("MIT", url("http://opensource.org/licenses/MIT"))

bintrayOrganization in ThisBuild := Some("apollographql")

lazy val root = project.in(file(".")).settings(
  name := "apollo-scalajs"
).aggregate(
  core, react
)

lazy val core = project

lazy val react = project.dependsOn(core)

lazy val tests = project.dependsOn(core, react)

lazy val example = project.dependsOn(core, react)
