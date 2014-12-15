package io.datalayer.randomforest

import io.datalayer.randomforest._
import breeze.linalg._
import scala.util.Random
import org.scalatest.FunSuite
import org.scalatest.ShouldMatchers

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
    val nb_loop = 100
}

/*
 Some test with pure scala data structures
*/
class ScalaStructureTest extends FunSuite with ShouldMatchers {
    val rand = new Random

    test("Test with Lists") {
        var t = List(0)
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = List.tabulate(StructureParams.vector_size){_ + rand.nextInt(100)}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Test with Arrays") {
        var t = Array(0)
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = Array.tabulate(StructureParams.vector_size){_ + rand.nextInt(100)}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Test with Vectors") {
        var t = Vector(0)
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = Vector.tabulate(StructureParams.vector_size){_ + rand.nextInt(100)}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Test with Seqs") {
        var t = Seq(0)
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = Seq.tabulate(StructureParams.vector_size){_ + rand.nextInt(100)}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }
}

/*
 Some test with Breeze data structures
*/
class BreezeStructureTest extends FunSuite with ShouldMatchers {
    test("Some test") {
        assert(true)
    }
}

/*
 Some test with RDD data structures
*/
class SparkStructureTest extends FunSuite with ShouldMatchers {
    test("Some test") {
        assert(true)
    }
}
