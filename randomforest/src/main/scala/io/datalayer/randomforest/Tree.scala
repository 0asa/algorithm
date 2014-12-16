package io.datalayer.randomforest

/*
Companion object
*/
object Tree {
  implicit def printParams(tree: Tree): String = {
    tree.printParams()
  }
}

/*
  Tree class: building a single decision tree
*/
class Tree( max_features: Int = 10,
            max_depth: Int = -1,
            min_samples_split: Int = 2) extends Learner {

  var root:Node = null
  var stack = new scala.collection.mutable.Stack[Node]
  var complexity:Int = 0

  private def printParams() : String = {
    val str_param:String = "max_features=" + max_features + ";" +
    "max_depth=" + max_depth + ";" +
    "min_samples_split=" + min_samples_split + ";" +
    "complexity=" + complexity + ";"
    str_param
  }

  def fit(x: Seq[Labeled]) = {
    val maps = x.groupBy(e => e.label.label)
    Node.nbclass = maps.size
    root = new Node
    root.samples = x

    stack.push(root)
    while (!stack.isEmpty) {
      val n = stack.pop()
      complexity += 1
      n.fit()
      if (n.canSplit()) {
        // generate partitions
        val partitions = n.samples.partition(i => i.input(n.split.attribute) < n.split.threshold)
        n.left = new Node(max_features,max_depth,min_samples_split)
        n.left.depth = n.depth + 1
        n.left.samples = partitions._1
        n.right = new Node(max_features,max_depth,min_samples_split)
        n.right.depth = n.depth + 1
        n.right.samples = partitions._2
        // push left and right children
        stack.push(n.left)
        stack.push(n.right)
        // clear samples: not optimal
        n.samples = null
      }
    }
  }

  def predict(x: Unlabeled) = {
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
