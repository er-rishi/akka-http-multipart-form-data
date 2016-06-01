name := "akka-http-multipart-data"

scalaVersion := "2.11.8"

version := "1.0"

libraryDependencies ++= {
  val akkaV = "2.4.3"
  val scalaTestV = "2.2.6"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaV,
    "org.scalatest" %% "scalatest" % scalaTestV % "test"
  )
}