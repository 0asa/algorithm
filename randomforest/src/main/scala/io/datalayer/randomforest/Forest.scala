package io.datalayer.randomforest

// Forest class: allow to build a forest of trees.
// the basics are available
// we will need to extend this furthermore
// depending on how far we can go...
class Forest(n_estimators: Int = 10, max_features: Int = 10, bootstrap: Boolean = false) {

  var trees: Array[Tree] = new Array[Tree](n_estimators)
  for (i <- 0 to (trees.length -1)) {
    trees(i) = new Tree(max_features)
  }

  def fit(x: Array[Array[Double]], y: Array[Double]) = {
    println("Forest.fit(X,Y)")
    for (i <- 0 to (trees.length -1)) {
      // TODO: bootstrap if needed
      trees(i).fit(x,y)
    }
  }

  // Predict for a single object
  def predict(x: Array[Double]) = {

  }

  // Predict for many objects
  def predict(x: Array[Array[Double]]) = {
    println("Forest.predict(X)")
    // TODO: store votes from each trees and return Array of votes
    for (i <- 0 to (trees.length -1)) {
      trees(i).predict(x)
    }
  }
}