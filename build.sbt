lazy val standardSettings = Seq(
  organization := "info.hupel.fork.com.verizon",
	sonatypeProfileName := "info.hupel",
  scalaVersion := "2.11.8",
  crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.0"),
  javacOptions += "-Xlint:unchecked",
  homepage := Some(url("https://github.com/larsrh/remotely/")),
  licenses := Seq(
    "Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.html")
  ),
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (version.value.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra := (
    <developers>
      <developer>
        <id>timperrett</id>
        <name>Timothy Perrett</name>
        <url>http://github.com/timperrett</url>
      </developer>
      <developer>
        <id>runarorama</id>
        <name>Runar Bjarnason</name>
        <url>http://github.com/runarorama</url>
      </developer>
      <developer>
        <id>stew</id>
        <name>Stew O'Connor</name>
        <url>http://github.com/stew</url>
      </developer>
      <developer>
        <id>ahjohannessen</id>
        <name>Alex Henning Johannessen</name>
        <url>https://github.com/ahjohannessen</url>
      </developer>
      <developer>
        <id>pchiusano</id>
        <name>Paul Chiusano</name>
        <url>https://github.com/pchiusano</url>
      </developer>
      <developer>
        <id>jedesah</id>
        <name>Jean-Rémi Desjardins</name>
        <url>https://github.com/jedesah</url>
      </developer>
      <developer>
        <id>larsrh</id>
        <name>Lars Hupel</name>
        <url>http://lars.hupel.info</url>
      </developer>
    </developers>
    <scm>
      <connection>scm:git:github.com/larsrh/remotely.git</connection>
      <developerConnection>scm:git:git@github.com:larsrh/remotely.git</developerConnection>
      <url>https://github.com/larsrh/remotely</url>
    </scm>
  ),
  credentials += Credentials(
    Option(System.getProperty("build.publish.credentials")) map (new File(_)) getOrElse (Path.userHome / ".ivy2" / ".credentials")
  ),
  autoAPIMappings := true
)

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

lazy val macroSettings = Seq(
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  libraryDependencies ++=
    Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value % "provided") ++ (
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, scalaMajor)) if scalaMajor == 10 => Seq("org.scalamacros" %% "quasiquotes" % "2.1.0")
        case _ => Nil
      }
    ),
  unmanagedSourceDirectories in Compile += {
    if (scalaBinaryVersion.value.startsWith("2.10"))
      (sourceDirectory in Compile).value / "macros" / s"scala-2.10"
    else
      (sourceDirectory in Compile).value / "macros" / s"scala-2.11"
  }
)

lazy val testSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scalatest"  %% "scalatest"  % "3.0.0"  % "test",
    "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
  )
)

lazy val root = project.in(file("."))
  .settings(standardSettings)
  .settings(noPublishSettings)
  .aggregate(core, examples, test, `test-server`)

lazy val core = project.in(file("core"))
  .settings(standardSettings)
  .settings(macroSettings)
  .settings(testSettings)
  .settings(
    scalacOptions ++= Seq(
      "-Ywarn-value-discard",
      "-Xlint",
      "-deprecation",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-language:postfixOps"
    ),
    libraryDependencies ++= {
      if (scalaVersion.value.startsWith("2.10"))
        Seq()
      else
        Seq("org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4")
    },
    libraryDependencies ++= Seq(
      "org.scodec"         %% "scodec-core"   % "1.10.3",
      "org.scodec"         %% "scodec-scalaz" % "1.4.1a",
      "org.scalaz"         %% "scalaz-core"   % "7.2.7",
      "org.scalaz.stream"  %% "scalaz-stream" % "0.8.6a",
      "org.apache.commons" % "commons-pool2"  % "2.4.2",
      "io.netty"           % "netty-handler"  % "4.1.5.Final",
      "io.netty"           % "netty-codec"    % "4.1.5.Final"
    )
  )

lazy val examples = project.in(file("examples"))
  .dependsOn(core)
  .settings(standardSettings)
  .settings(macroSettings)
  .settings(noPublishSettings)
  .settings(
    scalacOptions ++= Seq(
      "-language:existentials",
      "-language:postfixOps"
    )
  )

lazy val test = project.in(file("test"))
  .dependsOn(core)
  .settings(standardSettings)
  .settings(macroSettings)
  .settings(noPublishSettings)
  .settings(testSettings)

lazy val `test-server` = project.in(file("test-server"))
  .dependsOn(test)
  .settings(standardSettings)
  .settings(macroSettings)
  .settings(noPublishSettings)
  .settings(testSettings)


// Release stuff

import ReleaseTransformations._

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(action = Command.process("publishSigned", _), enableCrossBuild = true),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeReleaseAll", _), enableCrossBuild = true)
)


// Miscellaneous

cancelable in Global := true
parallelExecution in Global := false

addCommandAlias("validate",";compile;test:compile;test")
