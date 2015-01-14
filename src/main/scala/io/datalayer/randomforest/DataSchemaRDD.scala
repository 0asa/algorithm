package io.datalayer.randomforest

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SchemaRDD

/**
 * Created by manuel on 19/12/14.
 */

class DataSchemaRDD(sc: SparkContext) extends DataDNA {
  type data_type = Double
  type TX = RDD[Array[data_type]]
  type TY = RDD[(data_type, Long)]

  var inputs: RDD[Array[data_type]] = sc.emptyRDD[Array[data_type]]
  var labels: RDD[(data_type, Long)] = sc.emptyRDD[(data_type, Long)]

  def load(X: TX, Y: TY = sc.emptyRDD[(data_type, Long)]) {
    if (Y != sc.emptyRDD[(data_type, Long)]) {
      labeled = true
      labels = Y

      nb_classes = labels.map(_._1).distinct.count.toInt
    } else {
      labeled = false
    }

    inputs = X
    nb_objects = inputs.count.toInt
    nb_attributes = inputs.take(1).length
  }

  def loadCSV(uri: String, label: Int, delimiter:String) = {
//    val rawData = sc.textFile(uri).map(_.split(",").map(_.toDouble))
//
//    if (label > -1) {
//      labeled = true
//      labels = rawData.zipWithIndex.map{ case (o:Array[Double], i:Long) => o(label).->[Long](i) }
//    }
//    val test = rawData.map{ case (o: Array[Double]) => o.drop(label).toSeq }
//    test.map(Object.apply(_))
//    sqlContext.registerRDDAsTable()

    //inputs = sqlContext.sql("SELECT * FROM inputs")
  }

  def split(attr: Int, thr: Double): (DataSchemaRDD, DataSchemaRDD) = {
    val partOne = new DataSchemaRDD(sc)
    val partTwo = new DataSchemaRDD(sc)
    if (labeled) {
      var zipped = inputs.zip(labels).filter(_._1(attr) < thr)
      partOne.load(zipped.map(_._1), zipped.map(_._2))
      zipped = inputs.zip(labels).filter(_._1(attr) >= thr)
      partTwo.load(zipped.map(_._1), zipped.map(_._2))
    } else {
      partOne.load(inputs.filter(_(attr) < thr))
      partTwo.load(inputs.filter(_(attr) >= thr))
    }
    (partOne, partTwo)
  }

  def getObjects(indexes : Traversable[Int]) : DataSchemaRDD = {
    val newData = new DataSchemaRDD(sc)
    newData.load(inputs.zipWithIndex.filter(x => indexes.exists(x._2.toInt == _)).map(_._1),
      labels.filter(x => indexes.exists(x._2 == _)) )
    newData
  }

  def getAttributes(indexes : Traversable[Int]) : DataSchemaRDD = {
    val newData = new DataSchemaRDD(sc)
    val newInputs = inputs.map(a => a.zipWithIndex.filter(x => indexes.exists(_ == x._2)).map(_._1))
    newData.load(newInputs, labels)
    newData
  }

  def getLabels(indexes : Traversable[Int]) : TY = {
    labels.filter(x => indexes.exists(x._2.toInt == _))
  }

  def getValue(i: Int, j: Int) : data_type = {
    val obj = inputs.zipWithIndex.filter(_._2 == i).map(_._1).collect
    obj(0)(j)
  }

  def getCounts() : Map[data_type,Int] = {
    // TODO
    null
  }

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
