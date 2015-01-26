package io.datalayer.randomforest

case class CannotFitException(message: String) extends Exception(message)

/** A forest of decision trees
  *
  * @constructor create a new forest of decision trees.
  * @param n_estimators the number of trees in the forest
  * @param max_features the number of features tested at each node
  * @param max_depth the maximum depth of trees
  * @param min_samples_split the number of samples required in a splitting node
  * @param bootstrap activate boostrap sampling (not implemeted yet)
  */
class Forest( n_estimators: Int = 10,
              max_features: Int = 10,
              max_depth: Int = -1,
              min_samples_split: Int = 2,
              bootstrap: Boolean = false) extends Learner {

  var trees: Array[Tree] = new Array[Tree](n_estimators)
  for (i <- 0 to (trees.length - 1)) {
    trees(i) = new Tree(max_features, max_depth, min_samples_split)
  }

  override def toString: String = {
    val str_param:String = "n_estimators=" + n_estimators + ";" +
      "bootstrap=" + bootstrap + ";" +
      "max_features=" + max_features + ";" +
      "max_depth=" + max_depth + ";" +
      "min_samples_split=" + min_samples_split + ";"
    str_param
  }

  /** Fit method on DataDNA.
    *
    * TODO: abstract types contained in DataDNA
    *
    * @param x the labeled data DNA
    * @return nothing
    */
  def fit(x: DataDNA[Double,Seq[Double],Int]): Unit = {
    if (!x.labeled) {
      throw CannotFitException("Data must be labeled")
    }
    for (i <- 0 to (trees.length - 1)) {
      if (bootstrap) {
        // TODO: bootstrap if needed
      } else {
        trees(i).fit(x)
      }
    }
  }

  /** Fit method on Sequence of Labeled.
    *
    * TODO: this method is here for historical
    *       reasons and backward compatibility
    *
    * @param x a sequence of Labeled
    * @return nothing
    */
  def fit(x: Seq[Labeled]): Unit = {
    for (i <- 0 to (trees.length - 1)) {
      if (bootstrap) {
        // TODO: bootstrap if needed
      } else {
        trees(i).fit(x)
      }
    }
  }

  /** Predict method for a single Unlabeled.
    *
    * @param x an Unlabeled object
    * @return probas a vector of class probabilities
    */
  def predict(x: Unlabeled): Array[Double]= {
    def agg(x: Array[Double], y: Array[Double]) : Array[Double] = {
      val z = new Array[Double](x.length)
      for (i <- 0 until x.length) z(i) = x(i) + y(i)
      z
    }
    val probas = trees.map(_.predict(x)).reduce(agg)
    probas.map{ e => e/trees.length }
  }

  /** Predict method for a sequence of Unlabeled.
    *
    * @param x a sequence of Unlabeled objects
    * @return probas a vector of class probability vectors
    */
  def predict(x: Seq[Unlabeled]): Array[Array[Double]] = {
    x.map(predict(_)).toArray
  }

  /** Predict method for a single row of DataDNA.
    *
    * TODO: abstract types contained in RowDNA
    *
    * @param x a row of dataDNA
    * @return probas a vector of class probabilities
    */
  def predict(x: RowDNA[Double,Seq[Double], Int]): Array[Double] = {
    def agg(x: Array[Double], y: Array[Double]) : Array[Double] = {
      val z = new Array[Double](x.length)
      for (i <- 0 until x.length) z(i) = x(i) + y(i)
      z
    }
    val probas = trees.map(_.predict(x)).reduce(agg)
    probas.map{ e => e/trees.length }
  }

  /** Predict method for DataDNA.
    *
    * TODO: abstract types contained in DataDNA
    *
    * @param x a DataDNA set of objects
    * @return probas a vector of class probability vectors
    */
  def predict(x: DataDNA[Double,Seq[Double],Int]): Array[Array[Double]] = {
    x.map(predict(_)).toArray
  }

  /** Retrieve Variable importances
    *
    * TODO: implement this
    *
    * @return vimp a vector of variable importances
    */
  def importances(): Array[Double] = {
    // TODO
    Array.empty[Double]
  }

  def display() {
    for (i <- 0 to (trees.length - 1)) {
      println("TREE" + (i+1) + " #############")
      trees(i).display
    }
  }
}
