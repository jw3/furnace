organization := "com.rxthings"
name := "furnace"
version := "0.1-SNAPSHOT"
licenses +=("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))

scalaVersion := "2.11.7"
scalacOptions += "-target:jvm-1.8"

resolvers += "jw3 at bintray" at "https://dl.bintray.com/jw3/maven"

libraryDependencies := {
    val akkaVersion = "2.4.1"
    val akkaStreamVersion = "2.0.1"
    val elasticVersion = "2.1.1"

    Seq(
        "wiii" %% "awebapi" % "0.3",
        "com.rxthings" %% "akka-injects" % "0.2",
        "com.rxthings" %% "rxgpio-akka" % "0.0.1-0125d9769a19670ac3684e9c8f494daccb86e8d8-SNAPSHOT",

        "com.sksamuel.elastic4s" %% "elastic4s-core" % elasticVersion,
        "com.sksamuel.elastic4s" %% "elastic4s-streams" % elasticVersion,
        "com.sksamuel.elastic4s" %% "elastic4s-jackson" % elasticVersion,

        "gpio4s" %% "gpiocfg" % "0.1",
        "io.reactivex" %% "rxscala" % "0.25.0",

        "com.typesafe.akka" %% "akka-actor" % akkaVersion,
        "com.typesafe.akka" %% "akka-slf4j" % akkaVersion % Runtime,
        "com.typesafe.akka" %% "akka-stream-experimental" % akkaStreamVersion,
        "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",

        "org.scalatest" %% "scalatest" % "2.2.5" % Test,
        "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
        "org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % Test
    )
}
