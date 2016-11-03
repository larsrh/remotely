
organization in Global := "oncue.remotely"

scalaVersion in Global := "2.10.6"

crossScalaVersions in Global := Seq("2.10.6", "2.11.8", "2.12.0")

resolvers += Resolver.sonatypeRepo("releases")

lazy val remotely = project.in(file(".")).aggregate(core, examples, test, `test-server`).settings(publish := {})

lazy val core = project

lazy val examples = project dependsOn core

lazy val test = project dependsOn core

lazy val `test-server` = project dependsOn test

parallelExecution in Global := false

releaseCrossBuild := true

publishArtifact in (Compile, packageBin) := false

publish := ()

publishLocal := ()

// avoid having to reboot the JVM during the travis build.
addCommandAlias("validate",";compile;test:compile;test")
