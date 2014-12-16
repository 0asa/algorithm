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
//import breeze.linalg._

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
  /*
  def genArray(numInstances: Int = 10): (DenseMatrix[Double], DenseVector[Double]) = {
    val x = DenseMatrix.zeros[Double](numInstances,2)
    val y = DenseVector.zeros[Double](numInstances)
    val rand = new Random
    for (i:Int <- 0 until numInstances) {
      val a = rand.nextInt(10)
      val b = rand.nextInt(10)
      x(i, 0) = a
      x(i, 1) = b
      y(i) = if (a + b > 10) { 0 } else { 1 }
    }
    (x, y)
  }
  */

  def genLabeled(numInstances: Int = 10): Seq[Labeled] = {
    val rand = new Random
    val x: Seq[Labeled] = for (i:Int <- 0 until numInstances) yield {
      val a:Float = rand.nextInt(10)
      val b:Float = rand.nextInt(10)
      val c:Float = rand.nextInt(100)
      val d:Float = rand.nextFloat()
      val y = if ((a+b) > 0 && (a+b) < 6) { 0 } else if ((a+b) >= 6 && (a+b) < 12) { 1 } else { 2 }
      Labeled(Seq(a,b,c,d), Label(y))
    }
    x
  }

  def genUnlabeled(numInstances: Int = 10): Seq[Unlabeled] = {
    val rand = new Random
    val x: Seq[Unlabeled] = for (i:Int <- 0 until numInstances) yield {
      val a:Float = rand.nextInt(10)
      val b:Float = rand.nextInt(10)
      val c:Float = rand.nextInt(100)
      val d:Float = rand.nextFloat()
      Unlabeled(Seq(a,b,c,d))
    }
    x
  }
}

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

    println("Started")

    //val (x, y) = dataGenerator.genArray(10)
    val train = dataGenerator.genLabeled(10)
    val test = dataGenerator.genUnlabeled(10)
    val forest = new Forest()
    forest.fit(train)
    var probas = forest.predict(test)
    probas.foreach(println)    

    /*
    val conf = new SparkConf().setMaster("local").setAppName("Simple Application")
    val sc = new SparkContext(conf)

    val data = dataGenerator.genLabeledPoint(sc, 10000)
    data.take(10).foreach(println)

    val data2 = dataGenerator.genIndexedRowMatrix(sc, 10000)
    data2.rows.take(10).foreach(println)
    */

}
