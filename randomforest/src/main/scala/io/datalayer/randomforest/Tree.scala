package io.datalayer.randomforest

import scala.collection.mutable
import breeze.linalg._

// Tree class: building a single decision tree
// the basics are available
// we will need to extend this furthermore
// depending on what we will do...
class Tree(max_features: Int = 10) {

  var root = new Node
  var stack = new mutable.Stack[Node]

  def setParams() {
    println("Tree.setParams")
  }

  def fit(x: DenseMatrix[Double], y: DenseVector[Double]) = {
    println("+ Tree.fit()")

    stack.push(root) // stack init
    while (!stack.isEmpty) {
      val n = stack.pop()
      // TODO: pick a split
      // TODO: get partitions
      // TODO: create left and right nodes
      // TODO: push into the stack
    }
  }

  // Predict for a single object
  def predict(x: DenseVector[Double]): DenseVector[Double] = {
    predict(x.toDenseMatrix)
  }

  // Predict for many objects
  def predict(x: DenseMatrix[Double]): DenseVector[Double] = {
    println("+ Tree.predict()")
    var probas = DenseVector.fill[Double](x.rows, 0.5)
    probas
  }
}