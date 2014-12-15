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
    val vector_size = 50000
    val nb_loop = 1000
}

/*
 Some test with pure scala data structures
*/
class ScalaStructureTest extends FunSuite with ShouldMatchers {
    val rand = new Random

    test("Test with Lists") {
        var t = List.fill(StructureParams.vector_size){0}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = List.fill(StructureParams.vector_size){0}
            //t = List.tabulate(StructureParams.vector_size){_ + rand.nextInt(100)}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Test with Arrays") {
        var t = Array.fill(StructureParams.vector_size){0}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = Array.fill(StructureParams.vector_size){0}
            //t = Array.tabulate(StructureParams.vector_size){_ + rand.nextInt(100)}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Test with Vectors") {
        var t = Vector.fill(StructureParams.vector_size){0}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = Vector.fill(StructureParams.vector_size){0}
            //t = Vector.tabulate(StructureParams.vector_size){_ + rand.nextInt(100)}
        }
        val t2 = System.currentTimeMillis
        info("Took: " + (t2 - t1) + " msecs")
        assert(t.length == StructureParams.vector_size)
    }

    test("Test with Seqs") {
        var t = Seq.fill(StructureParams.vector_size){0}
        val t1 = System.currentTimeMillis
        for (i:Int <- 0 until StructureParams.nb_loop) {
            t = Seq.fill(StructureParams.vector_size){0}
            //t = Seq.tabulate(StructureParams.vector_size){_ + rand.nextInt(100)}
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
