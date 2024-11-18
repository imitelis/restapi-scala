// build.sbt
name := "restapi-scala"
version := "0.1.0"
scalaVersion := "3.5.2"

// Library versions
val tapirVersion = "1.11.8"
val http4sVersion = "0.23.29"
val slickVersion = "3.5.2"
val logbackVersion = "1.5.12"
val jsoniterVersion = "2.31.3"
val sqliteVersion = "3.47.0.0"

libraryDependencies ++= Seq(
  //
  "org.scala-lang" % "scala3-library_3" % scalaVersion.value,

  // Tapir for HTTP API definitions
  "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,

  // Http4s dependencies for building web services
  "org.http4s" %% "http4s-blaze-server" % "0.23.17",
  "org.http4s" %% "http4s-dsl" % "0.23.29",
  "org.http4s" %% "http4s-server" % "0.23.29",

  // Slick for database interaction
  "com.typesafe.slick" %% "slick" % "3.5.2",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.5.2",

  // Logging libraries
  "org.slf4j" % "slf4j-api" % "2.0.16",
  "ch.qos.logback" % "logback-classic" % "1.5.12",

  // SQLite JDBC for database interaction
  "org.xerial" % "sqlite-jdbc" % "3.47.0.0",

  // Circe for JSON handling
  "io.circe" %% "circe-core" % "0.14.10",
  "io.circe" %% "circe-generic" % "0.14.10",
  "io.circe" %% "circe-parser" % "0.14.10",

  // Testing
  "org.scalatest" %% "scalatest" % "3.2.9"
  
)

// Enable the `scala3` compiler options for Scala 3
scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")

// Resolving dependencies from the official repository
resolvers ++= Seq(
  Resolver.sonatypeRepo("public"), // Correct usage
  Resolver.mavenLocal              // Optional: Maven local repository
)

// Setup the source directories for your custom files (optional)
Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "scala"
Test / unmanagedSourceDirectories += baseDirectory.value / "src" / "test" / "scala"

// Ensure that the application runs in a separate JVM
run / fork := true

/* 
import sbtassembly.AssemblyPlugin.autoImport._

assemblyMergeStrategy in assembly := {
 case PathList("META-INF", _*) => MergeStrategy.discard
 case _                        => MergeStrategy.first
}
*/