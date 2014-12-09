package io.datalayer.randomforest

//import scala.collection.mutable

// Tree class: building a single decision tree
class Tree(max_features: Int = 10) extends Learner {

  var root = new Node
  //var stack = new mutable.Stack[Node]

  def setParams() {
    println("Tree.setParams")
  }

  def fit(x: Seq[Labeled]) = {    
    val maps = x.groupBy(e => e.label.label)
    root.nbclass = maps.size
    root.fit(x)    
  }
  
  def predict(x: Unlabeled) = {
    println(x)    
  }

  def predict(x: Seq[Unlabeled]): Seq[Double] = {
    //println("+ Tree.predict()")
    var probas = Seq.fill(x.length)(0.5)
    probas
  }

  def display() {
    root.display
  }
}