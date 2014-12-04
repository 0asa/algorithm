package io.datalayer.randomforest

import scala.collection.mutable

// Tree class: building a single decision tree
// the basics are available
// we will need to extend this furthermore
// depending on what we will do...
class Tree(max_features: Int = 10) {

  var root = new Node
  var stack = new mutable.Stack[Node]

  def fit(x: Array[Array[Double]], y: Array[Double]) = {
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
  def predict(x: Array[Double]) = {

  }

  // Predict for many objects
  def predict(x: Array[Array[Double]]) = {
    println("+ Tree.predict()")    
  }
}