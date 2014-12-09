package io.datalayer.randomforest

import io.datalayer.randomforest._
import breeze.linalg._
import org.scalatest.FunSuite
import org.scalatest.ShouldMatchers
/*
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

class SparkTest extends FunSuite with ShouldMatchers {
  test("Creating Spark Context") {
    val conf = new SparkConf().setMaster("local").setAppName("Simple Application")
    val sc = new SparkContext(conf)
    assert(1 == 1)
  }
}
*/

class NodeTest extends FunSuite with ShouldMatchers {
  //val (x, y) = dataGenerator.genArray(40)
  val train = dataGenerator.genLabeled(10)
  val test = dataGenerator.genUnlabeled(10)

  test("Node is a leaf") {    
    val node = new Node        
    assert(node.isLeaf == true)
  }

  test("Node.findRandomSplit") {
    val node = new Node
    node.findRandomSplit(train)
    assert(1 === 1)
  }

  test("Node.fit") {
    val node = new Node
    node.fit(train)
    node.display
    assert(node.split != null)
  }
  
}

class TreeTest extends FunSuite with ShouldMatchers {
  //val (x, y) = dataGenerator.genArray(40)
  val train = dataGenerator.genLabeled(20)
  val test = dataGenerator.genUnlabeled(10)
  test("Some tree test") {    
    val tree = new Tree
    tree.fit(train)
    //tree.display
    var probas = tree.predict(test)
    //probas.foreach(println)
    assert(1 === 1)
  }
}


class ForestTest extends FunSuite with ShouldMatchers {
  //val (x, y) = dataGenerator.genArray(40)
  val train = dataGenerator.genLabeled(10)
  val test = dataGenerator.genUnlabeled(10)
  test("Some forest test") {    
    val forest = new Forest
    forest.fit(train)
    var probas = forest.predict(test)
    //probas.foreach(println)
    assert(1 === 1)
  }
}

class MainTest extends FunSuite with ShouldMatchers {
  //val (x, y) = dataGenerator.genArray(40)
  val train = dataGenerator.genLabeled(10)
  val test = dataGenerator.genUnlabeled(10)
  test("Some more test to test scala") {    
    /*
    var dv = DenseVector.rand(10)
    val part = dv.toArray.partition(ex => ex < 0.5)
    println("part._1")
    part._1.foreach(println)
    println("part._2")
    part._2.foreach(println)
    */
    assert(1 === 1)
  }
}
