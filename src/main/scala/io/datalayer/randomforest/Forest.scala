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

  /** Method desc.
    *
    * @param
    * @return
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

  /** Method desc.
    *
    * @param
    * @return
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

  /** Method desc.
    *
    * @param
    * @return
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

  /** Method desc.
    *
    * @param
    * @return
    */
  def predict(x: Seq[Unlabeled]): Array[Array[Double]] = {
    x.map(predict(_)).toArray
  }

  /** Method desc.
    *
    * @param
    * @return
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

  /** Method desc.
    *
    * @param
    * @return
    */
  def predict(x: DataDNA[Double,Seq[Double],Int]): Array[Array[Double]] = {
    x.map(predict(_)).toArray
  }

  /** Method desc.
    *
    * @param
    * @return
    */
  def importances(): Array[Double] = {
    // TODO
    Array.empty[Double]
  }

  /** Method desc.
    *
    * @param
    * @return
    */
  def display() {
    for (i <- 0 to (trees.length - 1)) {
      println("TREE" + (i+1) + " #############")
      trees(i).display
    }
  }
}
