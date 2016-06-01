name :="akka-http-multipart-data"

scalaVersion :="2.11.8"

version := "1.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-experimental" % "2.0.2",
  "com.typesafe.akka" %% "akka-http-core-experimental" % "2.0.2",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.0.2"
)
