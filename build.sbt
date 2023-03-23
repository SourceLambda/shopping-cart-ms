ThisBuild / version := "0.0.1"

ThisBuild / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .enablePlugins(JavaServerAppPackaging, DockerPlugin)
  .settings(
    name             := "shopping-cart",
    idePackagePrefix := Some("dev.brave"),
    dockerRepository := Some("ghcr.io"),
    dockerUsername   := Some("hombre-x"),
    dockerExposedPorts ++= Seq(80, 8000, 8080),
    dockerBaseImage  := "openjdk:19",
    libraryDependencies ++= Seq(
      // Cats
      "org.typelevel" %% "cats-core"      % "2.9.0",
      "org.typelevel" %% "cats-effect"    % "3.4.8",
      "org.typelevel" %% "log4cats-core"  % "2.5.0",
      "org.typelevel" %% "log4cats-slf4j" % "2.5.0",
      "org.typelevel" %% "squants"        % "1.8.3",
      "org.typelevel" %% "kittens"        % "3.0.0",

      // Fs2
      "co.fs2" %% "fs2-core" % "3.6.1",

      // http4s
      "org.http4s" %% "http4s-dsl"          % "0.23.18",
      "org.http4s" %% "http4s-core"         % "0.23.18",
      "org.http4s" %% "http4s-circe"        % "0.23.18",
      "org.http4s" %% "http4s-ember-server" % "0.23.18",
      "org.http4s" %% "http4s-ember-client" % "0.23.18",

      // Skunk
      "org.tpolecat" %% "skunk-core" % "0.5.1",

      // Redis
      "dev.profunktor" %% "redis4cats-effects"  % "1.4.0",
      "dev.profunktor" %% "redis4cats-log4cats" % "1.4.0",

      // Monocle
      "dev.optics" %% "monocle-core" % "3.2.0",

      // Ciris
      "is.cir" %% "ciris" % "3.1.0",

      // Circe
      "io.circe" %% "circe-core"    % "0.14.5",
      "io.circe" %% "circe-generic" % "0.14.5",

      // Logback
      "ch.qos.logback" % "logback-classic" % "1.4.5"
    )
  )
