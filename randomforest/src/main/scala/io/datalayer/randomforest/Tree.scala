package io.datalayer.randomforest

// Tree class: building a single decision tree
class Tree(max_features: Int = 10, max_depth: Int = -1, min_samples_split: Int = 2) extends Learner {

  var root:Node = null
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
    root.fit(x)
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
