package io.datalayer.randomforest

import io.datalayer.randomforest._
import breeze.linalg._
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed.{IndexedRow, IndexedRowMatrix}
import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

//import org.scalatest.ShouldMatchers

//import org.apache.spark.SparkContext
//import org.apache.spark.SparkContext._
//import org.apache.spark.SparkConf
import scala.language.implicitConversions
import io.datalayer.common.SparkContextManager

class DataDNATest extends FunSuite {
  /*
  test("Input and labels with different sizes") {
    val data = new Data
    intercept[IncompatibleDataTypeException]{
      data.load(Seq(Seq(1.0,1.1),Seq(2.0,2.1),Seq(3.0,3.1)), Seq(0.0,0.1))
    }
  }
  */

  test("RowDNA test") {
    val rowtest = new RowDNA[Double,Seq[Double], Int](Seq(1.5,2.5,3.5),Some(1))

    assert(rowtest.nb_attributes == 3)
    assert(rowtest.isLabeled == true)
    assert(rowtest.attributes(0) == 1.5)
    assert(rowtest.attributes(1) == 2.5)
    assert(rowtest.attributes(2) == 3.5)
    assert(rowtest.label == Some(1))
  }

  test("First DataDNA test") {
    info("Going to be the coolest thing you've ever done!")

    val r1 = new RowDNA[Double,Seq[Double], Int](Seq(1.0,1.35,1.5,1.99),Some(0))
    val r2 = new RowDNA[Double,Seq[Double], Int](Seq(2.0,2.35,2.5,2.99),Some(1))
    val r3 = new RowDNA[Double,Seq[Double], Int](Seq(3.0,3.35,3.5,3.99),Some(2))

    val datatest = new Data(Seq(r1,r2,r3))

    assert(datatest.nb_objects == 3)
    assert(datatest.nb_attributes == 4)
    assert(datatest.nb_classes == 3)
    assert(datatest.labeled == true)
    assert(datatest.getAttribute(1) == List(1.35, 2.35, 3.35))
    assert(datatest.getLabel(1) == 1)
    //println(datatest.findRandomSplit)
    //println(datatest.split(0,1.5)._1)
    //println(datatest.split(0,1.5)._2)
    //println(datatest.getCounts)
    //println(datatest.getLabels(Seq(1,2)))
    //println(datatest.getObject(1))
    //println(datatest.getObjects(Seq(0,2)))
    //println(datatest.getAttributes(Seq(1,2)))
    //println(datatest)

    //println(datatest.map(_.attributes(0)))
    //println(datatest.map(_.label))
    //println(datatest.partition(_.attributes(1) < 2.5))

    /*
    val data = new Data
    data.load(Seq(Seq(1.0,1.1),Seq(2.0,2.1),Seq(3.0,3.1)))
    data.split(0, 1.5)
    data.load(Seq(Seq(1.0,1.1),Seq(2.0,2.1),Seq(3.0,3.1)), Seq(0.0,0.1,0.2))
    data.inputs.foreach(println)
    println(data.labels)

    //data.loadCSV

    // Test split
    val splited = data.split(0, 1.5)
    println(splited._1.labels)
    println(splited._2.labels)

    //data.describe

    data.inputs.map(x => x)

    assert(splited._1.labels == Seq(0.0))

    assert(data.getAttribute(0).inputs == Seq(Seq(1.0),Seq(2.0),Seq(3.0)))

    assert(data.getAttributes(Seq(0)).inputs == Seq(Seq(1.0),Seq(2.0), Seq(3.0)))

    assert(data.getObjects(Seq(0,1)).inputs == Seq(Seq(1.0,1.1),Seq(2.0,2.1)))
    assert(data.getObject(1).inputs == Seq(Seq(2.0,2.1)))

    assert(data.getLabels(Seq(0,1)) == Seq(0.0,0.1))
    assert(data.getLabel(1) == Seq(0.1))

    assert(data.getValue(0,0) == 1.0)
    assert(data.labeled == true)
    assert(data.nb_attributes == 2)
    assert(data.nb_objects == 3)
    */
  }

  test("DataRDD test") {
    /*
    info("Some serious stuff going on…")
    val sc = SparkContextManager.getSparkContext(8)
    val data = new DataSchemaRDD(sc)
    val train = sc.parallelize(Seq(Array(1.0,1.1), Array(2.0,2.1), Array(3.0,3.1)))
    val labels = sc.parallelize(Seq((1.0, 1.toLong), (2.0, 1.toLong), (1.0, 0.toLong)))

    data.load(train, labels)
    val split = data.split(0, 1.5)
    assert(Seq(Seq(1.0, 1.1)) === split._1.inputs.collect().toSeq.map(_.toSeq))
    assert(Seq(Seq(2.0, 2.1), Seq(3.0, 3.1)) === split._2.inputs.collect().toSeq.map(_.toSeq))

    assert(data.getAttributes(Seq(0)).inputs.collect.toSeq.map(_.toSeq) === Seq(Seq(1.0),Seq(2.0), Seq(3.0)))
    assert(data.getAttributes(Seq(1)).inputs.collect.toSeq.map(_.toSeq) === Seq(Seq(1.1),Seq(2.1), Seq(3.1)))

    assert(data.getObjects(Seq(2)).inputs.collect.toSeq.map(_.toSeq) === Seq(Seq(3.0, 3.1)))

    assert(data.inputs.take(1).take(1)(0)(1) === 1.1)

    //    data.loadCSV("/home/manuel/wrk/model/randomforest/src/test/resources/", 0)
    */
  }
}
