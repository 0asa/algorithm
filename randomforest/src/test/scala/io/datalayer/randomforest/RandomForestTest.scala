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
  val train = dataGenerator.genLabeled(40)
  val test = dataGenerator.genUnlabeled(10)
  val maps = train.groupBy(e => e.label.label)
  Node.nbclass = maps.size

  test("Node should be a leaf") {
    val node = new Node
    assert(node.isLeaf == true)
  }
  
  test("Node after fit should not be a leaf") {
    val node = new Node
    node.fit(train)
    assert(node.isLeaf === false)
  }


  test("Node findRandomSplit should find a split") {
    val node = new Node
    val split = node.findRandomSplit(train)
    println(train.length)
    assert(split.attribute > -1)
  }

  test("Node.fit") {
    val node = new Node    
    node.fit(train)
    //node.display
    assert(1 == 1)
  }

}

class TreeTest extends FunSuite with ShouldMatchers {
  //val (x, y) = dataGenerator.genArray(40)
  val train = dataGenerator.genLabeled(40)
  val test = dataGenerator.genUnlabeled(10)
  test("Some tree test") {
    val tree = new Tree
    tree.fit(train)
    //tree.display
    // predict for one sample
    var prob = tree.predict(test(0))    
    //println(prob(0) + "|" + prob(1))
    // predict for many samples
    var proball = tree.predict(test)
    //proball.foreach(e => println(e(0) + "|" + e(1)))
    val expectedClass = tree.predictLabel(test(0))
    val expectedClasses = tree.predictLabel(test)

    /*prob.foreach(x => print(x + " "))
    print("\n")
    println(expectedClass)
    print("\n------------\n")
    proball.take(3).foreach(x => {x.foreach(y => print(y + " "))
                          println("\n---")})
    expectedClasses.take(3).foreach(println)*/

    assert(prob === proball(0))
    assert(expectedClass === expectedClasses(0))
  }
}


class ForestTest extends FunSuite with ShouldMatchers {
  //val (x, y) = dataGenerator.genArray(40)
  val train = dataGenerator.genLabeled(40)
  val test = dataGenerator.genUnlabeled(100)

  test("Some forest test") {
    val forest = new Forest
    forest.fit(train)    
    //forest.display
    // predict for one sample
    var prob = forest.predict(test(0))
    //println(prob(0) + "|" + prob(1))  
    // predict for many samples  
    var proball = forest.predict(test)
    //proball.foreach(println)
    //proball.foreach(e => println(e(0) + "|" + e(1)))
    //proball.foreach(e => println(e.length))

    val expectedClass = forest.predictLabel(test(0))
    val expectedClasses = forest.predictLabel(test)
    /*prob.foreach(x => print(x + " "))
    print("\n")
    println(expectedClass)
    print("\n------------\n")
    proball.take(3).foreach(x => {x.foreach(y => print(y + " "))
      println("\n---")})
    expectedClasses.take(3).foreach(println)*/

    assert(expectedClass === expectedClasses(0))
    assert(prob === proball(0))    
  }
}

class MainTest extends FunSuite with ShouldMatchers {  
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
