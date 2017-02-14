name := "gameoflife"

version := "1.0"

scalaVersion := "2.12.1"

resolvers ++= Seq(
  Resolver.mavenLocal, Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"), "Bintray " at "https://dl.bintray.com/projectseptemberinc/maven"
)

val scalazVersion = "7.2.8"

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalaz" %% "scalaz-core" % scalazVersion
)
