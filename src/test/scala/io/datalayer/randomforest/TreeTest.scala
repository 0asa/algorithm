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

class TreeTest extends FunSuite {

  val train = TestParams.train
  val test = TestParams.test
  val evaluate = TestParams.evaluate

  test("Complexity should increase after fit") {
    val tree = new Tree(min_samples_split=10,max_features=25)
    val c1 = tree.complexity
    tree.fit(train)
    val c2 = tree.complexity
    assert(c1 < c2)
  }

  test("Prediction should be consistent") {
    val tree = new Tree(min_samples_split=5,max_features=25)
    tree.fit(train)
    var prob0 = tree.predict(test(0))
    var prob1 = tree.predict(test(1))
    var proball = tree.predict(test)
    assert(prob0 === proball(0))
    assert(prob1 === proball(1))
  }

  test("Labels should be consistent") {
    val tree = new Tree(min_samples_split=5,max_features=25)
    tree.fit(train)
    val expectedClass0 = tree.predictLabel(test(0))
    val expectedClass1 = tree.predictLabel(test(1))
    val expectedClasses = tree.predictLabel(test)
    assert(expectedClass0 === expectedClasses(0))
    assert(expectedClass1 === expectedClasses(1))
  }

  test("Accuracy should be > 0.5") {
    val tree = new Tree(min_samples_split=10,max_features=25)
    tree.fit(train)
    val accuracy = tree.predictEval(evaluate)._2
    info("Accuracy = " + accuracy)
    assert(accuracy >= 0.5)
  }

  test("Tree parameters test") {
    val tree = new Tree(min_samples_split=10,max_features=25)
    info(tree)
    assert("max_features=25;max_depth=-1;min_samples_split=10;complexity=0;" == tree.printParams)
  }
}
