package io.datalayer.randomforest

object Forest {
  implicit def printParams(forest: Forest): String = {
    forest.printParams()
  }
}

// Forest class: build a forest of trees.
class Forest(n_estimators: Int = 10, max_features: Int = 10, max_depth: Int = -1, min_samples_split: Int = 2, bootstrap: Boolean = false) extends Learner {

  var trees: Array[Tree] = new Array[Tree](n_estimators)
  for (i <- 0 to (trees.length - 1)) {
    trees(i) = new Tree(max_features, max_depth, min_samples_split)
  }

  def printParams() : String = {
    val str_param:String = "n_estimators=" + n_estimators + ";" +
      "bootstrap=" + bootstrap + ";" +
      "max_features=" + max_features + ";" +
      "max_depth=" + max_depth + ";" +
      "min_samples_split=" + min_samples_split + ";"
    str_param
  }

  def setParams() {
    println("Forest.setParams")
  }

  def fit(x: Seq[Labeled]) = {
    for (i <- 0 to (trees.length - 1)) {
      if (bootstrap) {
        // TODO: bootstrap if needed
      } else {
        trees(i).fit(x)
      }
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
      //probas(i).foreach(println)
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
