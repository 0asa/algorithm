package io.datalayer.randomforest

import io.datalayer.randomforest._
import breeze.linalg._
import scala.util.Random
import org.scalatest.FunSuite
import org.scalatest.ShouldMatchers
/*
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
*/

// Source:
// http://stackoverflow.com/questions/15436593/how-to-measure-and-display-the-running-time-of-a-single-test
// Could be useful somewhere else...
object Timer {
    def time[T](str: String)(thunk: => T): T = {
        print(str + "... ")
        val t1 = System.currentTimeMillis
        val x = thunk
        val t2 = System.currentTimeMillis
        println((t2 - t1) + " msecs")
        x
    }
}

object StructureParams {
    val vector_size = 10000
    val nb_loop = 1000
}

/*
import org.scalameter.api._
// Benchmark with scalameter.
// It generates a png plot in the tmp folder.
// Sample taken from:
// https://github.com/scalameter/scalameter/blob/master/src/test/scala/org/scalameter/RangeBenchmarks.scala
object RangeBenchmark
extends PerformanceTest {

  // configuration

  lazy val executor = SeparateJvmsExecutor(Warmer.Default(), Aggregator.min, new Measurer.Default)
  lazy val reporter = ChartReporter(ChartFactory.XYLine())
  lazy val persistor = Persistor.None

  // inputs

  val sizes = Gen.range("size")(300000, 1500000, 300000)

  val ranges = for {
    size <- sizes
  } yield 0 until size

  // tests

  performance of "Range" in {
    measure method "map" in {
      using(ranges) in {
        r => r.map(_ + 1)
      }
    }
  }
}
*/

/*
 Some test with pure scala data structures
*/
class ScalaStructureTest extends FunSuite {

    val rand = new Random

    test("Print StructureParams") {
        info("Number of loop: " + StructureParams.nb_loop)
        info("Structure size: " + StructureParams.vector_size)
        assert(true)
    }

    test("Fill on List") {
        var t = List.fill(StructureParams.vector_size){0}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = List.fill(StructureParams.vector_size){0}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Tabulate on List") {
        var t = List.fill(StructureParams.vector_size){0}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = List.tabulate(StructureParams.vector_size){_ => 0 + rand.nextInt(100)}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Reduce on List") {
        var t = List.tabulate(StructureParams.vector_size){_ => 0 + rand.nextInt(100)}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t.reduce(_+_)
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Partition on List") {
        var t = List.tabulate(StructureParams.vector_size){_ => 0 + rand.nextInt(100)}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t.partition(_ < rand.nextInt(100))
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Fill on Array") {
        var t = Array.fill(StructureParams.vector_size){0}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = Array.fill(StructureParams.vector_size){0}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Tabulate on Array") {
        var t = Array.fill(StructureParams.vector_size){0}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = Array.tabulate(StructureParams.vector_size){_ => 0 + rand.nextInt(100)}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Reduce on Array") {
        var t = Array.tabulate(StructureParams.vector_size){_ => 0 + rand.nextInt(100)}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t.reduce(_+_)
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Partition on Array") {
        var t = Array.tabulate(StructureParams.vector_size){_ => 0 + rand.nextInt(100)}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t.partition(_ < rand.nextInt(100))
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Fill on (scala) Vector") {
        var t = scala.collection.immutable.Vector.fill(StructureParams.vector_size){0}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = scala.collection.immutable.Vector.fill(StructureParams.vector_size){0}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Tabulate on (scala) Vector") {
        var t = scala.collection.immutable.Vector.fill(StructureParams.vector_size){0}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = scala.collection.immutable.Vector.tabulate(StructureParams.vector_size){_ => 0 + rand.nextInt(100)}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Reduce on (scala) Vector") {
        var t = scala.collection.immutable.Vector.tabulate(StructureParams.vector_size){_ => 0 + rand.nextInt(100)}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t.reduce(_+_)
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Partition on (scala) Vector") {
        var t = scala.collection.immutable.Vector.tabulate(StructureParams.vector_size){_ => 0 + rand.nextInt(100)}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t.partition(_ < rand.nextInt(100))
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Fill on Seq") {
        var t = Seq.fill(StructureParams.vector_size){0}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = Seq.fill(StructureParams.vector_size){0}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Tabulate on Seq") {
        var t = Seq.fill(StructureParams.vector_size){0}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = Seq.tabulate(StructureParams.vector_size){_ => 0 + rand.nextInt(100)}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Reduce on Seq") {
        var t = Seq.tabulate(StructureParams.vector_size){_ => 0 + rand.nextInt(100)}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t.reduce(_+_)
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Partition on Seq") {
        var t = Seq.tabulate(StructureParams.vector_size){_ => 0 + rand.nextInt(100)}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t.partition(_ < rand.nextInt(100))
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }
}

/*
 Some test with Breeze data structures
 See: https://github.com/scalanlp/breeze/wiki/Linear-Algebra-Cheat-Sheet
*/
class BreezeStructureTest extends FunSuite {

    val rand = new Random

    test("Print StructureParams") {
        info("Number of loop: " + StructureParams.nb_loop)
        info("Structure size: " + StructureParams.vector_size)
        assert(true)
    }

    test("Fill on (Breeze) Vector") {
        var t = Vector.fill(StructureParams.vector_size){0}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = Vector.fill(StructureParams.vector_size){0}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Tabulate on (Breeze) Vector") {
        var t = Vector.fill(StructureParams.vector_size){0}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = Vector.tabulate(StructureParams.vector_size){_ => 0 + rand.nextInt(100)}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Reduce on (Breeze) Vector") {
        var t = Vector.tabulate(StructureParams.vector_size){_ => 0 + rand.nextInt(100)}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t.reduce(_+_)
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Partition on (Breeze) Vector") {
        var t = Vector.tabulate(StructureParams.vector_size){_ => 0 + rand.nextInt(100)}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            // no partition available (as such)
            // need to cast to toArray (for instance)
            t.toArray.partition(_ < rand.nextInt(100))

        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Fill on DenseVector") {
        var t = DenseVector.zeros[Double](StructureParams.vector_size)
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = DenseVector.fill(StructureParams.vector_size){0}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Tabulate on DenseVector") {
        var t = DenseVector.zeros[Double](StructureParams.vector_size)
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = DenseVector.tabulate(StructureParams.vector_size){_ => 0 + rand.nextInt(100)}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Reduce on DenseVector") {
        var t = DenseVector.tabulate(StructureParams.vector_size){_ => 0 + rand.nextInt(100)}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t.reduce(_+_)
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Partition on DenseVector") {
        var t = DenseVector.tabulate(StructureParams.vector_size){_ => 0 + rand.nextInt(100)}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            // no partition available (as such)
            // need to cast to toArray (for instance)
            t.toArray.partition(_ < rand.nextInt(100))
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }
}

/*
 Some test with RDD data structures
*/
class SparkStructureTest extends FunSuite {
    test("Some test") {
        /*
        val conf = new SparkConf().setMaster("local").setAppName("Simple Application")
        val sc = new SparkContext(conf)
        */
        assert(true)
    }
}
