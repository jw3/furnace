
name := "furnace"

version := "0.1"

scalaVersion := "2.11.7"

libraryDependencies := {
    Seq(
        "org.scala-lang" % "scala-library" % "2.11.7",
        "org.scala-lang" % "scala-reflect" % "2.11.7",

        "wiii" %% "awebapi" % "0.2",
        "wiii" %% "akka-injects" % "0.1",

        "com.rxthings" %% "rxgpio-akka" % "0.0.1-0125d9769a19670ac3684e9c8f494daccb86e8d8-SNAPSHOT",

        "gpio4s" %% "gpiocfg" % "0.1",
        "io.reactivex" %% "rxscala" % "0.25.0",

        "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
        "com.typesafe.akka" %% "akka-actor" % "2.4.0",
        "com.typesafe.akka" %% "akka-slf4j" % "2.4.0" % Runtime,

        "org.scalatest" %% "scalatest" % "2.2.5" % Test,
        "com.typesafe.akka" %% "akka-testkit" % "2.4.0" % Test,
        "org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % Test
    )
}