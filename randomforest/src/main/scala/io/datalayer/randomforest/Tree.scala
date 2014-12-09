package io.datalayer.randomforest

import scala.collection.mutable
//import breeze.linalg._

// Tree class: building a single decision tree
class Tree(max_features: Int = 10) extends Learner {

  var root = new Node
  var stack = new mutable.Stack[Node]

  def setParams() {
    println("Tree.setParams")
  }

  def fit(x: Seq[Labeled]) = {
    //println("+ Tree.fit()")

    stack.push(root) // stack init
    while (!stack.isEmpty) {
      val n = stack.pop()
      // TODO: pick a split
      // TODO: get partitions
      // TODO: create left and right nodes
      // TODO: push into the stack
    }
  }
  
  def predict(x: Seq[Unlabeled]): Seq[Double] = {
    //println("+ Tree.predict()")
    var probas = Seq.fill(x.length)(0.5)
    probas
  }
}