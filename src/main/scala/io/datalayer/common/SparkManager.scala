package io.datalayer.common

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object SparkContextManager {
  var hasSC = false
  var sc:Any = 0

  def getSparkContext(workers: Int): SparkContext = {
    if (!hasSC) {
      val sparkConf = new SparkConf().setMaster("local[" + workers + "]").setAppName("SparkApp")
      sc = new SparkContext(sparkConf)
      hasSC = true
    }

    return sc.asInstanceOf[SparkContext]
  }

  def stopSparkContext() = {
    if (hasSC) {
      sc.asInstanceOf[SparkContext].stop()
    }
  }

  def restartSparkContext(workers: Int): SparkContext = {
    stopSparkContext()
    getSparkContext(workers)
  }
}
