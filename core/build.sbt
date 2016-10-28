
import common._

resolvers += Resolver.sonatypeRepo("public")

resolvers += Resolver.bintrayRepo("scalaz", "releases")

scalacOptions ++= Seq(
  "-Ywarn-value-discard",
  "-Xlint",
  "-deprecation",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps"
)

libraryDependencies ++= {
  if (scalaVersion.value.startsWith("2.10"))
    Seq()
  else
    Seq("org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4")
}

libraryDependencies ++= Seq(
  "org.scodec"         %% "scodec-core"   % "1.10.3",
  "org.scodec"         %% "scodec-scalaz" % "1.4.0a",
  "org.scalaz"         %% "scalaz-core"   % "7.2.7",
  "org.scalaz.stream"  %% "scalaz-stream" % "0.8.5a",
  "org.apache.commons" % "commons-pool2"  % "2.4.2",
  "io.netty"           % "netty-handler"  % "4.1.5.Final",
  "io.netty"           % "netty-codec"    % "4.1.5.Final"
)

common.macrosSettings

common.settings
