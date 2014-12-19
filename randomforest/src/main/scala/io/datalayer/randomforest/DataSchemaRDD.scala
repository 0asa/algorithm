package io.datalayer.randomforest

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SchemaRDD

/**
 * Created by manuel on 19/12/14.
 */

class DataSchemaRDD(sc: SparkContext) extends DataDNA {
  type data_type = Double
  type TX = SchemaRDD
  type TY = RDD[(data_type, Long)]

  val sqlContext = new org.apache.spark.sql.SQLContext(sc)
  import sqlContext.createSchemaRDD

  case class Object(obj: Array[data_type])

  var inputs: TX = sqlContext.createSchemaRDD(sc.parallelize(Array[Object](Object(Array[data_type](0.0)))))
  var labels: TY = sc.emptyRDD[(data_type, Long)]

  def load(X: TX, Y: TY = sc.emptyRDD[(data_type, Long)]) {
    println("SchemaRDD does not support loading from scala object, only from CSV file.")
  }

  def loadCSV(uri: String, label: Int) = {
    val rawData = sc.textFile(uri).map(_.split(" ").map(_.toDouble))

    if (label > -1) {
      labeled = true
      labels = rawData.zipWithIndex.map{ case (o:Array[Double], i:Long) => o(label).->[Long](i) }
    }
    rawData.map{ case (o: Array[Double]) => Object(o.drop(label)) }.registerTempTable("inputs")
    inputs = sqlContext.sql("SELECT * FROM inputs")
  }

  def split(attr: Int, thr: data_type): (DataSchemaRDD, DataSchemaRDD) = {
    //    val partOne = new DataRDD(sc)
    //    val partTwo = new DataRDD(sc)
    //    val zipped = inputs.zip(labels).partition(i => i._1(attr) < thr)
    //    partOne.load(zipped._1.unzip._1, zipped._1.unzip._2)
    //    partTwo.load(zipped._2.unzip._1, zipped._2.unzip._2)
    //    (partOne, partTwo)
    (this, this)
  }

  def getObjects(indexes : Traversable[Int]) : TX = {
    //    indexes.map{i => inputs(i)}.toSeq
    inputs
  }


  def getAttributes(indexes : Traversable[Int]) : TX = {
    //    (for (i <- indexes) yield { inputs.map(_(i)) }).toSeq
    inputs
  }

  def getLabels(indexes : Traversable[Int]) : TY = {
    //    indexes.map{i => labels(i)}.toSeq
    labels
  }

  def getValue(i: Int, j: Int) : data_type = { 0.0 }

  def describe {
    //    if (inputs.isEmpty) {
    //      println("There is no data.")
    //      return
    //    }

    //    println("\nData description :")
    //    println("------------------")
    //    println("Is there labels ?: " + labeled)
    //    println("Number of objects: " + inputs.length)
    //    println("Number of attributes: " + inputs(0).length)
    //
    //    for (i <- 0 to (inputs(0).length - 1) ) {
    //      println("Attribute " + i + " :")
    //      val mean = inputs.map(_(i)).reduce(_ + _) / inputs.length
    //      println("Mean: " + mean)
    //      println("Sample Variance: " + inputs.map( (x:Seq[Double]) => Math.pow(x(i) - mean, 2)).sum /(inputs.length - 1) )
    //    }
  }
}
