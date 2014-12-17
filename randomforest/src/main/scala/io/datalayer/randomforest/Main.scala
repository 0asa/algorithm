package io.datalayer.randomforest

//import org.apache.spark.rdd.RDD
//import org.apache.spark.SparkContext
//import org.apache.spark.SparkContext._
//import org.apache.spark.SparkConf
//import org.apache.spark.mllib.linalg.{Vectors, Vector}
//import org.apache.spark.mllib.regression.LabeledPoint
//import org.apache.spark.mllib.linalg.distributed.{IndexedRow, IndexedRowMatrix, RowMatrix}
//import scala.collection.mutable
//import scala.util.Random
//import breeze.linalg._

// Helper to compute various metrics...
// will be extend later...
object Metrics {
  def accuracy(pred: Seq[Label], labels: Seq[Label]): Double = {
    pred.zip(labels).filter(x => x._1 == x._2).size.toDouble / pred.length
  }

  def auc() = {}
  def entropy() = {}
  def gini() = {}
  def variance() = {}
}

object Main extends App {
    println("Started...")
    val t1 = System.currentTimeMillis

    val train = dataGenerator.genLabeled(100)
    val test = dataGenerator.genUnlabeled(10)
    val forest = new Forest()
    forest.fit(train)
    var probas = forest.predict(test)
    probas.foreach(println)

    val t2 = System.currentTimeMillis
    println("Time: " + (t2 - t1) + " ms")



    /*
    val conf = new SparkConf().setMaster("local").setAppName("Simple Application")
    val sc = new SparkContext(conf)

    val data = dataGenerator.genLabeledPoint(sc, 10000)
    data.take(10).foreach(println)

    val data2 = dataGenerator.genIndexedRowMatrix(sc, 10000)
    data2.rows.take(10).foreach(println)
    */

}
