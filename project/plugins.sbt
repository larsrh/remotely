
resolvers += Resolver.url(
  "bintray-sbt-plugin-releases",
    url("http://dl.bintray.com/content/sbt/sbt-plugin-releases"))(
        Resolver.ivyStylePatterns)

resolvers += Resolver.url(
  "tpolecat-sbt-plugin-releases",
    url("http://dl.bintray.com/content/tpolecat/sbt-plugin-releases"))(
        Resolver.ivyStylePatterns)

addSbtPlugin("me.lessis"         % "bintray-sbt" % "0.3.0")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.0")
addSbtPlugin("com.typesafe.sbt"  % "sbt-site"    % "0.8.1")
addSbtPlugin("com.typesafe.sbt"  % "sbt-ghpages" % "0.5.3")
addSbtPlugin("org.tpolecat"      % "tut-plugin"  % "0.4.5")
addSbtPlugin("com.jsuereth"      % "sbt-pgp"     % "1.0.0")
addSbtPlugin("org.wartremover"   % "sbt-wartremover" % "1.1.1")
addSbtPlugin("com.eed3si9n"      % "sbt-assembly"    % "0.11.2")
