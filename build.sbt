val scala3V = "3.0.0-M3"
val zioV = "1.0.4-2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "ziox",
    version := "0.1.0",
    scalaVersion := scala3V,
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio"                % zioV,
      "dev.zio" %% "zio-test"           % zioV % "test",
      "dev.zio" %% "zio-test-sbt"       % zioV % "test"
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
