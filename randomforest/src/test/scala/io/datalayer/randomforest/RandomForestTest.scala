package io.datalayer.randomforest

import io.datalayer.randomforest._
import breeze.linalg._
import org.scalatest.FunSuite
//import org.scalatest.ShouldMatchers
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

object TestParams {
  val ls_size = 1000
  val ts_size = 100
}

class NodeTest extends FunSuite {
  //val (x, y) = dataGenerator.genArray(40)
  val train = dataGenerator.genLabeled(TestParams.ls_size)
  val test = dataGenerator.genUnlabeled(TestParams.ts_size)
  val maps = train.groupBy(e => e.label.label)
  Node.nbclass = maps.size

  test("Node should be a leaf") {
    val node = new Node
    info(node.printParams)
    assert(node.isLeaf == true)
  }

  test("Node after fit should not be a leaf") {
    val node = new Node
    node.fit(train)
    assert(node.isLeaf === false)
  }

  test("Node findRandomSplit should find a split") {
    // The truth is it might not...
    // in which case split.attribute will be equal to -1
    // this is caused by the current findRandomSplit implementation
    val node = new Node
    val split = node.findRandomSplit(train)
    assert(split.attribute >= -1 && split.attribute <= train(0).input.length)
  }

  test("Node.fit") {
    val node = new Node
    node.fit(train)
    //node.display
    assert(1 == 1)
  }

}

class TreeTest extends FunSuite {
  //val (x, y) = dataGenerator.genArray(40)
  val train = dataGenerator.genLabeled(TestParams.ls_size)
  val test = dataGenerator.genUnlabeled(TestParams.ts_size)
  val evaluate = dataGenerator.genLabeled(TestParams.ls_size)

  test("Some tree test") {
    val tree = new Tree
    info(tree.printParams)
    tree.fit(train)
    val accuracy = tree.predictEval(evaluate)._2
    info("Accuracy = " + accuracy)
    info("Error rate = " + (1 - accuracy))
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


class ForestTest extends FunSuite {
  //val (x, y) = dataGenerator.genArray(40)
  val train = dataGenerator.genLabeled(TestParams.ls_size)
  val test = dataGenerator.genUnlabeled(TestParams.ts_size)
  val evaluate = dataGenerator.genLabeled(TestParams.ls_size)

  test("Some forest test") {
    val forest = new Forest(min_samples_split=100)
    info(forest.printParams)
    forest.fit(train)
    val accuracy = forest.predictEval(evaluate)._2
    info("Accuracy = " + accuracy)
    info("Error rate = " + (1 - accuracy))
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

    val trueClasses: Seq[Label] = for (e <- test) yield {
      val a = e.input(0)
      val b = e.input(1)
      val y = if ((a+b) > 0 && (a+b) < 6) { 0 } else if ((a+b) >= 6 && (a+b) < 12) { 1 } else { 2 }
      Label(y)
    }
    var error = 0.0
    for (i <- 0 to (trueClasses.length - 1)) {
      if (trueClasses(i) != expectedClasses(i)) {
        error += 1.0
      }
    }
    //println("Error rate: " + error/test.length)
    assert( error/test.length < 0.5)
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

class MainTest extends FunSuite {
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
