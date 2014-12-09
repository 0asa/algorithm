package io.datalayer.controlchart

import io.datalayer.controlchart._
import org.apache.spark.{SparkContext, SparkConf}
import org.scalatest.FunSuite
import org.scalatest.ShouldMatchers


case class Measure(m: Float)


class ControlChartTest extends FunSuite with ShouldMatchers {
  test("ControlChart should give us 1 outlier") {
    val testArray = Array[Float](5, 5, 5, 5, 5, 5, 5, 5, 9)
    val cc = new ControlChart(testArray)
    cc.summary()
    assert(cc.outliers.length === 1)
  }
}

class ReadCSVTest extends FunSuite with ShouldMatchers {
  test("ReadCSV should read csv containing 101 lines") {
    val testFile = new ReadCSV("src/test/resources/test.csv")
    assert(testFile.getColumn(0).length === 101)
  }
}

class ControlChartPipeTest extends FunSuite with ShouldMatchers {
  test("test controlchart within a pipeline") {
    val conf = new SparkConf().setMaster("local").setAppName("Simple Application")
    val sc = new SparkContext(conf)
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


