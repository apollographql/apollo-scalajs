organization in ThisBuild := "com.apollographql"

scalaVersion in ThisBuild := "2.12.8"

scalacOptions in ThisBuild ++= Seq("-feature", "-deprecation")

licenses in ThisBuild += ("MIT", url("http://opensource.org/licenses/MIT"))

bintrayOrganization in ThisBuild := Some("apollographql")

lazy val root = project.in(file(".")).withId("apollo-scalajs").aggregate(
  core, react
).settings(
  publish := {},
  publishLocal := {}
)

lazy val core = project.enablePlugins(BintrayPlugin)

lazy val react = project.dependsOn(core).enablePlugins(BintrayPlugin)

lazy val tests = project.dependsOn(core, react)

lazy val example = project.dependsOn(core, react)
