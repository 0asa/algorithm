package io.datalayer.randomforest

import breeze.linalg._

// Forest class: allow to build a forest of trees.
// the basics are available
// we will need to extend this furthermore
// depending on how far we can go...
class Forest(n_estimators: Int = 10, max_features: Int = 10, bootstrap: Boolean = false) {

  var trees: Array[Tree] = new Array[Tree](n_estimators)
  for (i <- 0 to (trees.length - 1)) {
    trees(i) = new Tree(max_features)
  }

  def setParams() {
    println("Forest.setParams")
  }

  def fit(x: DenseMatrix[Double], y: DenseVector[Double]) = {
    println("Forest.fit(X,Y)")
    for (i <- 0 to (trees.length - 1)) {
      // TODO: bootstrap if needed
      trees(i).fit(x, y)
    }
  }

  // Predict for a single object
  def predict(x: DenseVector[Double]): DenseVector[Double] = {
    predict(x.toDenseMatrix)
  }

  // Predict for many objects
  def predict(x: DenseMatrix[Double]): DenseVector[Double] = {
    println("Forest.predict(X)")
    // TODO: store votes from each trees and return Array of votes
    var probas = DenseVector.fill[Double](x.rows, 0.0)
    for (i <- 0 to (trees.length - 1)) {
      // TODO : do something...
      probas += trees(i).predict(x)
    }
    probas.map { x => x / n_estimators }
  }
}