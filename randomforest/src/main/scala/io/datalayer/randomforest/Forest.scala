package io.datalayer.randomforest

// Forest class: build a forest of trees.
class Forest(n_estimators: Int = 10, max_features: Int = 10, bootstrap: Boolean = false) extends Learner {

  var trees: Array[Tree] = new Array[Tree](n_estimators)
  for (i <- 0 to (trees.length - 1)) {
    trees(i) = new Tree(max_features)
  }

  def setParams() {
    println("Forest.setParams")
  }

  def fit(x: Seq[Labeled]) = {    
    for (i <- 0 to (trees.length - 1)) {
      // TODO: bootstrap if needed
      trees(i).fit(x)
    }
  }

  def predict(x: Seq[Unlabeled]): Seq[Double] = {      
    var probas = Seq.fill(x.length)(0.0)
    for (i <- 0 to (trees.length - 1)) {      
      probas = (probas,trees(i).predict(x)).zipped.map(_ + _)
    }
    probas.map { x => x / n_estimators }    
  }
}