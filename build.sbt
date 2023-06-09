ThisBuild / version := "0.0.5"

ThisBuild / scalaVersion := "3.3.0"

lazy val root = (project in file("."))
  .enablePlugins(JavaServerAppPackaging, DockerPlugin)
  .settings(
    name             := "shopping-cart-ms",
    idePackagePrefix := Some("com.sourcelambda"),
    dockerRepository := Some("hombrexgsp"),
    dockerExposedPorts ++= Seq(80, 8000, 8080),
    dockerBaseImage  := "openjdk:19",
    libraryDependencies ++= Seq(
      // Cats
      "org.typelevel" %% "cats-core"      % "2.9.0",
      "org.typelevel" %% "cats-effect"    % "3.5.0",
      "org.typelevel" %% "log4cats-core"  % "2.5.0",
      "org.typelevel" %% "log4cats-slf4j" % "2.5.0",
      "org.typelevel" %% "kittens"        % "3.0.0",

      // Fs2
      "co.fs2" %% "fs2-core" % "3.7.0",

      // http4s
      "org.http4s" %% "http4s-dsl"          % "0.23.20",
      "org.http4s" %% "http4s-core"         % "0.23.20",
      "org.http4s" %% "http4s-circe"        % "0.23.20",
      "org.http4s" %% "http4s-ember-server" % "0.23.20",
      "org.http4s" %% "http4s-ember-client" % "0.23.20",

      // Redis
      "dev.profunktor" %% "redis4cats-effects"  % "1.4.1",
      "dev.profunktor" %% "redis4cats-log4cats" % "1.4.1",

      // Monocle
      "dev.optics" %% "monocle-core" % "3.2.0",

      // Ciris
      "is.cir" %% "ciris" % "3.1.0",

      // Circe
      "io.circe" %% "circe-core"    % "0.14.5",
      "io.circe" %% "circe-generic" % "0.14.5",

      // Logback
      "ch.qos.logback" % "logback-classic" % "1.4.7"
    ),
    
    scalacOptions ++= Seq(
      "-Wunused:all"
    )
  )
