organization := "me.lessis"

name := "gq"

version := "0.1.0-SNAPSHOT"

description := "Interface with graphite in style"

crossScalaVersions := Seq("2.10.4", "2.11.0")

scalaVersion := crossScalaVersions.value.head

libraryDependencies ++= Seq("net.databinder.dispatch" %% "dispatch-json4s-native" % "0.11.1")

