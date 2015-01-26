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

  var root:Node = null
  var stack = new scala.collection.mutable.Stack[Node]
  var complexity:Int = 0

  override def toString: String = {
    val str_param:String = "max_features=" + max_features + ";" +
    "max_depth=" + max_depth + ";" +
    "min_samples_split=" + min_samples_split + ";" +
    "complexity=" + complexity + ";"
    str_param
  }

  /** Method desc.
    *
    * @param
    * @return
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
      // but will need to adapt the Node class
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
        // clear samples: not optimal
        n._samples = null
      }
    }

  }

  /** Method desc.
    *
    * @param
    * @return
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
        // clear samples: not optimal
        n.samples = null
      }
    }
  }

  /** Method desc.
    *
    * @param
    * @return
    */
  def predict(x: Unlabeled): Array[Double] = {
    root.predict(x)
  }

  /** Method desc.
    *
    * @param
    * @return
    */
  def predict(x: RowDNA[Double,Seq[Double], Int]): Array[Double] = {
    root.predict(x)
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
  def predict(x: Seq[Unlabeled]): Array[Array[Double]] = {
    x.map(predict(_)).toArray
  }

  /** Method desc.
    *
    * @param
    * @return
    */
  def importances(): Array[Double] = {
    // TODO: scan all the nodes
    // use information stored in n.split
    Array.empty[Double]
  }

  /** Method desc.
    *
    * @param
    * @return
    */
  def display() {
    root.display
  }
}
