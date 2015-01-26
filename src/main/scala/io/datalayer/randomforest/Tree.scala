package io.datalayer.randomforest

/** A single decision tree
  *
  * @constructor create a new decison tree.
  * @param max_features the number of features tested at each node
  * @param max_depth the maximum depth
  * @param min_samples_split the number of samples required in a splitting node
  */
class Tree( max_features: Int = 10,
            max_depth: Int = -1,
            min_samples_split: Int = 2) extends Learner {

  var root:Node = null // the root node of the tree
  var stack = new scala.collection.mutable.Stack[Node] // a stack of Node
  var complexity:Int = 0 // the number of nodes in the tree

  /** Fit method on DataDNA.
    *
    * TODO: abstract types contained in DataDNA
    *
    * @param x the labeled data DNA
    * @return nothing
    */
  def fit(x: DataDNA[Double,Seq[Double],Int]): Unit = {
    Node.nbclass = x.nb_classes
    root = new Node
    root._samples = x

    stack.push(root)
    while (!stack.isEmpty) {
      val n = stack.pop()
      complexity += 1
      n._fit()
      if (n._canSplit()) {
        // generate partitions
        val partitions = n._samples.split(n.split.attribute,n.split.threshold)
        n.left = new Node(max_features,max_depth,min_samples_split)
        n.left.depth = n.depth + 1
        n.left._samples = partitions._1
        n.right = new Node(max_features,max_depth,min_samples_split)
        n.right.depth = n.depth + 1
        n.right._samples = partitions._2
        // push left and right children
        stack.push(n.left)
        stack.push(n.right)
        // clear samples: not optimal (should try to use lazy/views)
        n._samples = null
      }
    }

  }

  /** Fit method on Sequence of Labeled.
    *
    * TODO: this method is here for historical
    *       reasons and backward compatibility
    *       this method will disappear (does not use DataDNA)
    *
    * @param x a sequence of Labeled
    * @return nothing
    */
  def fit(x: Seq[Labeled]): Unit = {
    val maps = x.groupBy(e => e.label.label)
    Node.nbclass = maps.size
    root = new Node
    root.samples = x

    stack.push(root)
    while (!stack.isEmpty) {
      val n = stack.pop()
      complexity += 1
      n.fit()
      if (n.canSplit()) {
        // generate partitions
        val partitions = n.samples.partition(i => i.input(n.split.attribute) < n.split.threshold)
        n.left = new Node(max_features,max_depth,min_samples_split)
        n.left.depth = n.depth + 1
        n.left.samples = partitions._1
        n.right = new Node(max_features,max_depth,min_samples_split)
        n.right.depth = n.depth + 1
        n.right.samples = partitions._2
        // push left and right children
        stack.push(n.left)
        stack.push(n.right)
        // clear samples: not optimal (should try to use lazy/views)
        n.samples = null
      }
    }
  }

  /** Predict method for a single Unlabeled.
    *
    * TODO: this method will disappear (does not use DataDNA)
    *
    * @param x an Unlabeled object
    * @return probas a vector of class probabilities
    */
  def predict(x: Unlabeled): Array[Double] = {
    root.predict(x)
  }

  /** Predict method for a sequence of Unlabeled.
    *
    * TODO: this method will disappear (does not use DataDNA)
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
    root.predict(x)
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

  /** Retrieve Variable importances for a single tree
    *
    * TODO: implement this
    *
    * @return vimp a vector of variable importances
    */
  def importances(): Array[Double] = {
    // TODO: scan all the nodes
    // use information stored in node.split
    Array.empty[Double]
  }

  def display() {
    root.display
  }

  override def toString: String = {
    val str_param:String = "max_features=" + max_features + ";" +
    "max_depth=" + max_depth + ";" +
    "min_samples_split=" + min_samples_split + ";" +
    "complexity=" + complexity + ";"
    str_param
  }
}
