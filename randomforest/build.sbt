name := "datalayer-model-random-forest"

version := "1.0"

scalaVersion := "2.11.0"

//resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

// libraryDependencies += "com.storm-enroute" %% "scalameter" % "0.6" % "test"

// testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")

// parallelExecution in Test := false

//libraryDependencies  ++= Seq(
            // other dependencies here
            //"org.scalanlp" %% "breeze" % "0.8.1",
            // native libraries are not included by default. add this if you want them (as of 0.7)
            // native libraries greatly improve performance, but increase jar sizes.
            //"org.scalanlp" %% "breeze-natives" % "0.8.1"
//)

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.2.0"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "1.2.0"

libraryDependencies += "org.apache.spark" %% "spark-mllib" % "1.2.0"
