organization in ThisBuild := "me.shadaj"

version in ThisBuild := "0.1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.12.2"

lazy val core = project

lazy val example = project.dependsOn(core)