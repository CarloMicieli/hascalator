resolvers += Classpaths.sbtPluginReleases

addSbtPlugin("org.scoverage"      % "sbt-scoverage"         % "1.3.5")
addSbtPlugin("org.scoverage"      % "sbt-coveralls"         % "1.1.0")
addSbtPlugin("org.scalastyle"    %% "scalastyle-sbt-plugin" % "0.8.0")
addSbtPlugin("org.scalariform"    % "sbt-scalariform"       % "1.6.0")
addSbtPlugin("de.heikoseeberger"  % "sbt-header"            % "1.6.0")
addSbtPlugin("com.typesafe.sbt"   % "sbt-git"               % "0.8.5")
addSbtPlugin("com.updateimpact"   % "updateimpact-sbt-plugin" % "2.1.1")
addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.2.10")
