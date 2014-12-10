package io.datalayer.randomforest

// Forest class: build a forest of trees.
class Forest(n_estimators: Int = 10, max_features: Int = 10, _bootstrap: Boolean = false) extends Learner {
  val bootstrap:Boolean = _bootstrap

  var trees: Array[Tree] = new Array[Tree](n_estimators)
  for (i <- 0 to (trees.length - 1)) {
    trees(i) = new Tree(max_features)
  }

  def setParams() {
    println("Forest.setParams")
  }

  def fit(x: Seq[Labeled]) = {
    for (i <- 0 to (trees.length - 1)) {
      if (bootstrap) {
        // TODO: bootstrap if needed  
      }      
      trees(i).fit(x)
    }
  }

  def predict(x: Unlabeled): Array[Double]= {
    var probas = new Array[Double](trees(0).root.nbclass)
    for (i <- 0 to (trees.length - 1)) {      
      probas = (probas,trees(i).predict(x)).zipped.map(_ + _)
    }    
    probas.map{ e => e/trees.length }
  }

  def predict(x: Seq[Unlabeled]): Array[Array[Double]] = {
    var probas = new Array[Array[Double]](x.length)
    for (i <- 0 to (x.length - 1)) {
      probas(i) = predict(x(i))
    }    
    probas
  }

  def display() {
    for (i <- 0 to (trees.length - 1)) {
      println("TREE" + (i+1) + " #############")
      trees(i).display
    }
  }
}