package io.datalayer.randomforest

import scala.collection.mutable
import scala.util.Random

//abstract class GenericNode(left: Option[GenericNode], right: Option[GenericNode])
case class Split(attribute: Int, threshold: Double)

// Node class
class Node {
  
  var left: Node = null
  var right: Node = null
  var split: Split = null
  var depth: Int = 1
  var votes: Array[Double] = null
  var nbclass:Int = 0 

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
    val gs: Double = p.length*gini(p) - (pl.length*gini(pl) + pr.length*gini(pr))    
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
  
  def findRandomSplit(x: Seq[Labeled]): Split = {    
    val rand = new Random    
    var att = Random.nextInt(x(0).input.length)        
    val att_vector = rand.shuffle(x.map(i => i.input(att)))
    var th = math.min(att_vector(0),att_vector(1)) + (math.abs(att_vector(0) - att_vector(1)) / 2.0)        
    Split(att,th)
  }
  
  def fit(x: Seq[Labeled]): Unit = {
    if (x.length > 10) { // That's a dummy stopping criterion
      split = findRandomSplit(x)
      val partitions = x.partition(i => i.input(split.attribute) < split.threshold)
      left = new Node()
      left.depth = depth + 1
      left.nbclass = nbclass
      left.fit(partitions._1)
      right = new Node()
      right.depth = depth + 1
      right.nbclass = nbclass
      right.fit(partitions._2) 
    } else {
      // create votes
      val maps = x.groupBy(e => e.label.label)
      val counts = maps.map(e => { (e._1, e._2.length) } )
      votes = new Array[Double](nbclass)      
      for (e <- counts) {                      
        votes(e._1) = e._2
      } 
      // TODO : normalize votes
    }
  }

  def predict(x: Seq[Unlabeled]) = {}

  def display() {    
    //for (i:Int <- 0 until depth) print("   ")
    //println("+ Depth: " + depth)
    if (split != null) {
      for (i:Int <- 0 until depth) print("   ")
      println("+ Split: " + split.attribute + " < " + split.threshold)
    }
    if (!isLeaf) {
      left.display
      right.display
    } else {
      if (votes != null) { 
        for (i:Int <- 0 until depth) print("   ")
        println(votes(0) + " | " + votes(1))        
      }
    }
  } 
}