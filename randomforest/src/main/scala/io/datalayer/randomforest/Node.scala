package io.datalayer.randomforest

import scala.collection.mutable
import scala.util.Random
import breeze.linalg._

abstract class GenericNode(left: Option[GenericNode], right: Option[GenericNode])

// Node class: currently, I'm trying to keep things
// consistent with a fit and predict
// while it has a different meaning at the node level
// in a classic decision tree, it could make sense
// if we extend this to do more "complicated" stuff
class Node {
  // TODO: add left and right child
  var left: Node = null
  var right: Node = null

  def setLeft(n: Node) {
    left = n
  }

  def setRight(n: Node) {
    right = n
  }

  def setChild(l: Node, r: Node) {
    left = l
    right = r
  }

  def isLeaf() = {
    if (left == null && right == null) {
      true
    } else {
      false
    }
  }
  
  def findRandomSplit(x: DenseMatrix[Double], y: DenseVector[Double]) {
    val rand = new Random
    val att = Random.nextInt(x.cols)    
    val max = x(::,att).max
    val min = x(::,att).min
    // Pick a random threshold in [min and max]
    // not the best strategy, yet.
    // Will see what we can do with RDDs later.
    val th = min + (Random.nextDouble() * (max-min))
    // Something similar could be used to split the data:
    // + maybe we won't need breeze in the end.
    val partitions = x(::,att).toArray.partition(ex => ex < th)
    //println("<  " + th) 
    //partitions._1.foreach(println)
    //println(">= " + th)
    //partitions._2.foreach(println)
  }
  
  // TODO: set a split type (at random, best split, ...)
  def fit(x: Array[Array[Double]], y: Array[Double]) = {}
  def predict(x: Array[Array[Double]]) = {}
}