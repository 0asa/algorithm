package io.datalayer.randomforest

import io.datalayer.randomforest._
import breeze.linalg._
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed.{IndexedRow, IndexedRowMatrix}
import org.scalatest.FunSuite
import scala.io.Source

import scala.collection.mutable.ArrayBuffer

//import org.scalatest.ShouldMatchers

//import org.apache.spark.SparkContext
//import org.apache.spark.SparkContext._
//import org.apache.spark.SparkConf
import scala.language.implicitConversions
import io.datalayer.common.SparkContextManager

/*
object TestParams {
  val ls_size = 500
  val ts_size = 1000
  val train = dataGenerator.genLabeled(ls_size,50)
  val test = dataGenerator.genUnlabeled(ts_size,50)
  val evaluate = dataGenerator.genLabeled(ts_size,50)
}
*/

class StandaloneTest extends FunSuite {
  test("Standalone dla Random Forest") {
    val trees = new Forest(min_samples_split=1000,n_estimators=10,max_features=2)
    //val labeled = dataGenerator.genLabeled(100,50)
    //val labeled = new Data
    //labeled.loadCSV("/Users/botta/wrk/datalayer/events/20150415-hadoop-summit/src/data.csv",0)
    //labeled.describe
    //trees.fit(labeled)    

    val x = Source.fromFile("/Users/botta/wrk/datalayer/events/20150415-hadoop-summit/src/data.csv").getLines() map {
      line => val fields = line.split(" ")
        Labeled(
          fields.drop(1).map(_.toFloat).toList.toSeq,
          Label(fields(0).toInt)
          )
    }

    trees.fit(x.toSeq.slice(0,4000))
    val accuracy = trees.predictEval(x.toSeq.slice(6000,7000))
    info("Accuracy = " + accuracy._2)
    //trees.display
    //accuracy._1.foreach(println)
    
    
    
    //info("test")
    //assert(1 == 1)
  }
}

