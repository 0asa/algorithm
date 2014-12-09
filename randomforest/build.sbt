name := "datalayer-model-random-forest"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies  ++= Seq(
            // other dependencies here
            "org.scalanlp" %% "breeze" % "0.8.1",
            // native libraries are not included by default. add this if you want them (as of 0.7)
            // native libraries greatly improve performance, but increase jar sizes.
            "org.scalanlp" %% "breeze-natives" % "0.8.1"
)

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.1.1"