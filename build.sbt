enablePlugins(BintrayPlugin)

organization in ThisBuild := "com.apollographql"

version in ThisBuild := "0.3.0"

scalaVersion in ThisBuild := "2.12.4"

scalacOptions in ThisBuild ++= Seq("-feature", "-deprecation")

licenses in ThisBuild += ("MIT", url("http://opensource.org/licenses/MIT"))

bintrayOrganization in ThisBuild := Some("apollographql")

lazy val core = project

lazy val example = project.dependsOn(core)
