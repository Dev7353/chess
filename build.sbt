name := "chess"

version := "0.1"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-swing" % "2.0.0-M2",
  "com.typesafe.akka" %% "akka-actor" % "2.5.12",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % Test,
  "com.typesafe.akka" %% "akka-http" % "10.1.1",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.1" % Test,
  "com.typesafe.akka" %% "akka-stream" % "2.5.12",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.12" % Test,
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.1",
  "com.typesafe.slick" %% "slick" % "3.2.3",
  "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "0.19",
  "mysql" % "mysql-connector-java" % "5.1.34",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.3.0",
  "io.netty" % "netty-all" % "4.1.25.Final"
)
