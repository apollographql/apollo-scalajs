organization in ThisBuild := "com.apollographql"

version in ThisBuild := "0.1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.12.3"

scalacOptions in ThisBuild ++= Seq("-feature", "-deprecation")

lazy val core = project

lazy val example = project.dependsOn(core)
