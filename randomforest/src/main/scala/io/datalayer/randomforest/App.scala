package io.datalayer.randomforest

//import org.apache.spark.rdd.RDD
//import org.apache.spark.SparkContext
//import org.apache.spark.SparkContext._
//import org.apache.spark.SparkConf
//import org.apache.spark.mllib.linalg.{Vectors, Vector}
//import org.apache.spark.mllib.regression.LabeledPoint
//import org.apache.spark.mllib.linalg.distributed.{IndexedRow, IndexedRowMatrix, RowMatrix}
import scala.collection.mutable
import scala.util.Random
import io.datalayer.randomforest.Node
import io.datalayer.randomforest.Tree
import io.datalayer.randomforest.Forest

// Helper to generate dummy data
// for testing purposes
object dataGenerator {
  /*
  def genLabeledPoint(sc: SparkContext, numInstances: Int = 10) = {
    val arr = new Array[LabeledPoint](numInstances)
    val rand = new Random
    for(i <- 0 until numInstances){
      val x = rand.nextInt(10)
      val y = rand.nextInt(10)
      val label = if (x+y > 10) { 0 } else { 1 }
      arr(i) = new LabeledPoint(label, Vectors.dense(x,y))
    }
    sc.parallelize(arr)
  }
  */
  /*
  def genIndexedRowMatrix(sc: SparkContext, numInstances: Int = 10) = {
    val arr = new Array[IndexedRow](numInstances)
    val rand = new Random
    for(i <- 0 until numInstances){
      val x = rand.nextInt(10)
      val y = rand.nextInt(10)
      val label = if (x+y > 10) { 0 } else { 1 }
      arr(i) = new IndexedRow(i, Vectors.dense(x,y,label))
    }
    val rdd = sc.parallelize(arr)
    val mat: IndexedRowMatrix = new IndexedRowMatrix(rdd)
    mat
  }
  */
  def genArray(numInstances: Int = 10) : (Array[Array[Double]], Array[Double]) = {
    val x = new Array[Array[Double]](numInstances)
    val y = new Array[Double](numInstances)
    val rand = new Random
    for(i <- 0 until numInstances) {
      val a = rand.nextInt(10)
      val b = rand.nextInt(10)
      x(i) = Array[Double](a,b)
      y(i) = if (a+b > 10) { 0 } else { 1 }
    }
    (x,y)
  }

}

// Helper to compute various metrics...
// will be extend later...
object Metrics {
  def accuracy() = {}
  def auc() = {}
  def entropy() = {}
  def gini() = {}
  def variance() = {}
}


object App {
  def main(args: Array[String]) {
    println("Started")

    val (x,y) = dataGenerator.genArray(100)
    val forest = new Forest()
    forest.fit(x,y)
    forest.predict(x)
    /*
    val conf = new SparkConf().setMaster("local").setAppName("Simple Application")
    val sc = new SparkContext(conf)

    val data = dataGenerator.genLabeledPoint(sc, 10000)
    data.take(10).foreach(println)

    val data2 = dataGenerator.genIndexedRowMatrix(sc, 10000)
    data2.rows.take(10).foreach(println)
    */
  }
}