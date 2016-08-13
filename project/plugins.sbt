resolvers += Classpaths.sbtPluginReleases

addSbtPlugin("org.scoverage"     % "sbt-scoverage"         % "1.0.1")
addSbtPlugin("org.scoverage"     % "sbt-coveralls"         % "1.0.0.BETA1")
addSbtPlugin("org.scalastyle"   %% "scalastyle-sbt-plugin" % "0.8.0")
addSbtPlugin("org.scalariform"   % "sbt-scalariform"       % "1.6.0")
addSbtPlugin("de.heikoseeberger" % "sbt-header"            % "1.6.0")
addSbtPlugin("org.tpolecat"      % "tut-plugin"            % "0.4.3")
addSbtPlugin("com.typesafe.sbt"  % "sbt-git"               % "0.8.5")
