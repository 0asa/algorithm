package io.datalayer.randomforest

case class CannotFitException(message: String) extends Exception(message)

/*
  Companion object
*/
object Forest {
  implicit def printParams(forest: Forest): String = {
    forest.printParams()
  }
}



/*
  Forest class: build a forest of trees.
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

  private def printParams() : String = {
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

  def fit(x: DataDNA): Unit = {
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

  def fit(x: Seq[Labeled]): Unit = {
    for (i <- 0 to (trees.length - 1)) {
      if (bootstrap) {
        // TODO: bootstrap if needed
      } else {
        trees(i).fit(x)        
      }
    }
  }
  
  def predict(x: Unlabeled): Array[Double]= {    
    def agg(x: Array[Double], y: Array[Double]) : Array[Double] = {
      val z = new Array[Double](x.length)
      for (i <- 0 until x.length) z(i) = x(i) + y(i)
      z
    }    
    val probas = trees.map(_.predict(x)).reduce(agg)    
    probas.map{ e => e/trees.length }
  }

  def predict(x: Seq[Unlabeled]): Array[Array[Double]] = {    
    x.map(predict(_)).toArray
  }

  // TODO
  def predict(x: DataDNA): Array[Array[Double]] = {
    var probas = new Array[Array[Double]](x.nb_objects)
    probas
    //x.map(predict(_)).toArray
  }

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
