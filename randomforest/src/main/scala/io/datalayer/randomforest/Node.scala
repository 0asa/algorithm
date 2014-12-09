package io.datalayer.randomforest

import scala.collection.mutable
import scala.util.Random
//import breeze.linalg._

//abstract class GenericNode(left: Option[GenericNode], right: Option[GenericNode])
//case class Split(attribute: Int, threshold: Float)

// Node class
class Node {
  
  var left: Node = null
  var right: Node = null
  var attribute = 0
  var threshold = 0.0

  def setLeft(n: Node) {
    left = n
  }

  def setRight(n: Node) {
    right = n
  }

  def setChild(l: Node, r: Node) {
    left = l
    right = r
  }

  def isLeaf() = {
    if (left == null && right == null) {
      true
    } else {
      false
    }
  }

  def gini(p: Seq[Labeled]): Double = {    
    var gi:Double = 0.0
    val maps = p.groupBy(e => e.label.label)
    val counts = maps.map(e => { (e._1, e._2.length) } )
    val total:Double = counts.map(e => e._2).reduce(_+_)
    for (e <- counts) {
      gi += (e._2/total) * (e._2/total)
    }    
    1 - gi
  }

  // Compute the gini impurity score
  def giniScore(p: Seq[Labeled], pl: Seq[Labeled], pr: Seq[Labeled]) : Double = {
    // weight the scores    
    val gs: Double = p.length*gini(p) - (pl.length*gini(pl) + pr.length*gini(pr))
    println(gs/p.length)
    gs/p.length
  }

  def infogain(p: Seq[Labeled]) : Double = {    
    var ig:Double = 0.0
    val maps = p.groupBy(e => e.label.label)
    val counts = maps.map(e => { (e._1, e._2.length) } )
    val total:Double = counts.map(e => e._2).reduce(_+_)
    for (e <- counts) {
      ig += (e._2/total) * math.log(e._2/total)
    }    
    - ig
  }

  // Compute the information gain score
  def infogainScore(p: Seq[Labeled], pl: Seq[Labeled], pr: Seq[Labeled]) : Double = {
    val is: Double = p.length*infogain(p) - (pl.length*infogain(pl) + pr.length*infogain(pr))    
    is/p.length
  }
  
  def findRandomSplit(x: Seq[Labeled]) {
    val rand = new Random    
    val att = Random.nextInt(x(1).input.length)    
    println(att)
    val att_vector = rand.shuffle(x.map(i => i.input(att)))
    val th = math.min(att_vector(0),att_vector(1)) + (math.abs(att_vector(0) - att_vector(1)) / 2.0)    
    val partitions = x.partition(i => i.input(att) < th)
    //println("ATT" + att +  " < " + th )
    //partitions._1.foreach(println)
    //println("ATT" + att +  " >= " + th )
    //partitions._2.foreach(println)
    giniScore(x,partitions._1,partitions._2)
    infogainScore(x,partitions._1,partitions._2)
    //val max = x(::,att).max
    //val min = x(::,att).min
    // Pick a random threshold in [min and max]
    // not the best strategy, yet.
    // Will see what we can do with RDDs later.
    //val th = min + (Random.nextDouble() * (max-min))
    // Something similar could be used to split the data:
    // + maybe we won't need breeze in the end.
    //val partitions = x(::,att).toArray.partition(ex => ex < th)
    //println("<  " + th) 
    //partitions._1.foreach(println)
    //println(">= " + th)
    //partitions._2.foreach(println)
    
  }
  
  def fit(x: Seq[Labeled]) = {}
  def predict(x: Seq[Unlabeled]) = {}
}