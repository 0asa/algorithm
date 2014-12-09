package io.datalayer.randomforest

// Tree class: building a single decision tree
class Tree(max_features: Int = 10) extends Learner {

  var root = new Node

  def setParams() {
    println("Tree.setParams")
  }

  def fit(x: Seq[Labeled]) = {
    val maps = x.groupBy(e => e.label.label)
    root.nbclass = maps.size
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