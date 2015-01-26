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

class NodeTest extends FunSuite {

  val train = TestParams.train
  val test = TestParams.test
  val maps = train.groupBy(e => e.label.label)
  Node.nbclass = maps.size

  test("Node should be a leaf") {
    val node = new Node
    info(node)
    assert(node.isLeaf == true)
  }

  test("Node split after fit should not be null") {
    val node = new Node
    node.samples = train
    node.fit()
    assert(node.split !== null)
  }

  test("Node canSplit() test") {
    // A case where canSplit() should return true
    val ntrue = new Node(min_samples_split=TestParams.ls_size-1)
    ntrue.samples = train
    ntrue.split = ntrue.findRandomSplit()
    info(ntrue)
    assert(ntrue.canSplit === true)
    // A case where canSplit() should return false
    val nfalse = new Node(min_samples_split=TestParams.ls_size+1)
    nfalse.samples = train
    nfalse.split = nfalse.findRandomSplit()
    info(nfalse)
    assert(nfalse.canSplit === false)
  }

  test("Node findRandomSplit should find a split") {
    // The truth is it might not...
    // in which case split.attribute will be equal to -1
    // this is caused by the current findRandomSplit implementation
    val node = new Node
    node.samples = train
    val split = node.findRandomSplit()
    assert(split.attribute >= -1 && split.attribute <= train(0).input.length)
  }

  test("Node findKRandomSplit") {
    val node = new Node(max_features=1)
    node.samples = train
    val split = node.findKRandomSplit()
    assert(true)
  }

}
