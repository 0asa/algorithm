package io.datalayer.randomforest

import scala.collection.mutable

object Tree {
  implicit def printParams(tree: Tree): String = {
    tree.printParams()
  }
}

// Tree class: building a single decision tree
class Tree(max_features: Int = 10, max_depth: Int = -1, min_samples_split: Int = 2) extends Learner {

  var root:Node = null
  var stack = new mutable.Stack[Node]
  var complexity:Int = 0 // will store the number of nodes

  def printParams() : String = {
    val str_param:String = "max_features=" + max_features + ";" +
    "max_depth=" + max_depth + ";" +
    "min_samples_split=" + min_samples_split + ";"
    str_param
  }

  def setParams() {
    println("Tree.setParams")
  }

  def fit(x: Seq[Labeled]) = {
    val maps = x.groupBy(e => e.label.label)
    Node.nbclass = maps.size
    root = new Node
    root.samples = x
    //root.fit()

    stack.push(root) // stack init
    while (!stack.isEmpty) {
      val n = stack.pop()
      n.fit()
      if (n.canSplit()) {
        // gen partitions
        val partitions = n.samples.partition(i => i.input(n.split.attribute) < n.split.threshold)
        n.left = new Node(max_features,max_depth,min_samples_split)
        n.left.depth = n.depth + 1
        n.left.samples = partitions._1
        n.right = new Node(max_features,max_depth,min_samples_split)
        n.right.depth = n.depth + 1
        n.right.samples = partitions._2
        // push left and right nodes
        stack.push(n.left)
        stack.push(n.right)
        // clear samples
        n.samples = null
      } // else do nothing
    }
  }

  def predict(x: Unlabeled) = {
    //println(x)
    root.predict(x)
  }

  def predict(x: Seq[Unlabeled]): Array[Array[Double]] = {
    var probas = new Array[Array[Double]](x.length)
    for (i <- 0 to (x.length - 1)) {
      probas(i) = predict(x(i))
    }
    probas
  }

  def display() {
    root.display
  }
}
