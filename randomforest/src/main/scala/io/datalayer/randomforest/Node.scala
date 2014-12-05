package io.datalayer.randomforest

import scala.collection.mutable
import scala.util.Random
import breeze.linalg._

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
    
  }
  
  // TODO: set a split type (at random, best split, ...)
  def fit(x: Array[Array[Double]], y: Array[Double]) = {}
  def predict(x: Array[Array[Double]]) = {}
}