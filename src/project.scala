// project.scala

// language
//> using scala 3.5.1

// dependencies
//> using dep com.softwaremill.sttp.tapir::tapir-core:1.11.8
//> using dep com.softwaremill.sttp.tapir::tapir-http4s-server:1.11.8
//> using dep com.softwaremill.sttp.tapir::tapir-jsoniter-scala:1.11.8
//> using dep com.softwaremill.sttp.tapir::tapir-swagger-ui-bundle:1.11.8
//> using dep com.github.plokhotnyuk.jsoniter-scala::jsoniter-scala-macros:2.31.2
//> using dep org.http4s::http4s-blaze-server:0.23.17
//> using dep org.http4s::http4s-dsl:0.23.29
//> using dep org.http4s::http4s-server:0.23.29
//> using dep ch.qos.logback:logback-classic:1.5.12
//> using dep org.slf4j:slf4j-api:2.0.16
//> using dep com.typesafe.slick::slick:3.5.2
//> using dep com.typesafe.slick::slick-hikaricp:3.5.2
//> using dep org.xerial:sqlite-jdbc:3.47.0.0

// files
//> using file ./bases/
//> using file ./config/
//> using file ./controllers/
//> using file ./models/
//> using file ./services/