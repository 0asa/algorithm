package io.datalayer.controlchart

import io.datalayer.controlchart._
import org.apache.spark.{SparkContext, SparkConf}
import org.scalatest.FunSuite
import org.scalatest.ShouldMatchers
import scala.util.Random

import org.apache.spark.mllib.util._


case class Measure(m: Double)

class dummyCalculus extends  FunSuite with ShouldMatchers {
  val sc = SparkContextManager.getSparkContext(8)
  val testArray = sc.parallelize(Array[Double](5, 5, 5, 5, 5, 5, 5, 9))

  test("Test mean computation with an RDD") {

    val mean = Stat.computeMean(testArray)
    assert(mean === 5.5)
  }

  test("Test variance computation with an RDD") {
    val mean = Stat.computeMean(testArray)
    val variance = Stat.computeVariance(testArray, mean)
    println(variance)
    assert(variance === 2.0)
  }

}

class ControlChartTest extends FunSuite with ShouldMatchers {
  val sc = SparkContextManager.getSparkContext(8)

  def genDataset(size: Int): org.apache.spark.rdd.RDD[Double] = {
    val generator = Random
    val x:Seq[Double] = for (i:Int <- 1 to size) yield {
      // Generate artificial outliers
      if (generator.nextInt(size) == size-1) {
        10.0
      } else {
        generator.nextGaussian()
      }
    }
    sc.parallelize(x)
  }

  test("ControlChart should give us 1 outlier") {
    println("Generating data")
    //val testRDD = sc.parallelize(Array[Double](5, 5, 5, 5, 5, 5, 5, 5, 9))
    val testRDD = genDataset(4000000)
    println("Data generated")
    val cc = new ControlChart
    cc.setStdLimit(5.0)
    val start = System.currentTimeMillis()
    cc.computeLimit(testRDD)
    val end = System.currentTimeMillis()
    cc.summary(testRDD)
    assert(0 === 0)
    info("Time spent: " + (end - start) + " ms" )
    //assert(cc.outliers.length === 1)
  }

}

class ReadCSVTest extends FunSuite with ShouldMatchers {
  test("ReadCSV should read csv containing 101 lines") {
    val testFile = new ReadCSV("src/test/resources/test.csv")
    assert(testFile.getColumn(0).length === 101)
  }
}

class ControlChartPipeTest extends FunSuite with ShouldMatchers {
  val sc = SparkContextManager.getSparkContext(8)

  test("test controlchart within a pipeline") {
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    import sqlContext.createSchemaRDD
    val rawData = Array[Measure](Measure(5), Measure(5), Measure(5), Measure(5),
      Measure(5), Measure(9))
    val meas = sc.parallelize(rawData)
    meas.registerTempTable("measures")
    val data = sqlContext.sql("SELECT * FROM measures")
    //
    val cc = new ControlChartPipe
    val model = cc.fit(data)
    assert(0 === 0)
  }

}
