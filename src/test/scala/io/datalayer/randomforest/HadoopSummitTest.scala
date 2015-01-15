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
    val trees = new Forest(min_samples_split=10,n_estimators=10,max_features=10)    
    //val labeled = dataGenerator.genLabeled(100,50)
    //val labeled = new Data
    //labeled.loadCSV("/Users/botta/wrk/datalayer/events/20150415-hadoop-summit/src/data.csv",0)
    //labeled.describe
    //trees.fit(labeled)        
        
    // READ FROM CSV
    // TODO: there is something wrong here
    // the following returns a Stream instead of a Seq/List
    // there must be a better way ;-)
    val d = Source.fromFile("/Users/botta/wrk/datalayer/events/20150415-hadoop-summit/src/data.csv").getLines() map {
      line => val fields = line.split(" ")
        Labeled(
          fields.drop(1).map(_.toFloat).toList.toSeq,
          Label(fields(0).toInt)
          )
    }
    val data = d.toList // Stream to List    

    val train = data.toSeq.slice(1000,2000)        
    trees.fit(train)    

    val test1 = data.toSeq.slice(0,1000).toList    
    println(test1.length)
    var accuracy = trees.predictEval(test1)
    info("Accuracy = " + accuracy._2)
    
    val test2 = data.toSeq.slice(1000,2000).toList
    println(test2.length)
    var accuracy2 = trees.predictEval(test2)
    info("Accuracy = " + accuracy2._2)
    
  }
}

  class StandaloneSplitTest extends FunSuite {
    test("Standalone dla Random Forest with Split") {    

     // READ FROM CSV
    // TODO: there is something wrong here
    // the following returns a Stream instead of a Seq/List
    // there must be a better way ;-)
    val d = Source.fromFile("/Users/botta/wrk/datalayer/events/20150415-hadoop-summit/src/data.csv").getLines() map {
      line => val fields = line.split(" ")
        Labeled(
          fields.drop(1).map(_.toFloat).toList.toSeq,
          Label(fields(0).toInt)
          )
    }
    val data = d.toList // Stream to List     

    val trees1 = new Forest(min_samples_split=10,n_estimators=10,max_features=10) 
    val data1 = data.toSeq.slice(0,1000)//.toList.toSeq    
    trees1.fit(data1)    
    info("acc: " + trees1.predictEval(data.toSeq.slice(1000,2000))._2)
    
    val trees2 = new Forest(min_samples_split=10,n_estimators=10,max_features=10) 
    val data2 = data.toSeq.slice(1000,2000)
    trees2.fit(data2)
    info("acc: " + trees2.predictEval(data.toSeq.slice(0,1000))._2)

    // WARNING: forest parameters are not updated here
    val ensemble = new Forest() 
    ensemble.trees = Array.concat(trees1.trees, trees2.trees)
    info("acc: " + ensemble.predictEval(data.toSeq.slice(0,2000))._2)

  }
}

