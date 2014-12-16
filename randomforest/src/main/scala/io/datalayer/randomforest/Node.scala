package io.datalayer.randomforest

import scala.collection.mutable
import scala.util.Random

//abstract class GenericNode(left: Option[GenericNode], right: Option[GenericNode])
case class Split(attribute: Int, threshold: Double)

object Node {
  var nbclass: Int = 0
}

// Node class
class Node(max_features: Int = 10, max_depth: Int = -1, min_samples_split: Int = 2) {

  var left: Node = null
  var right: Node = null
  var split: Split = null
  var depth: Int = 1
  var votes: Array[Double] = null
  var samples: Seq[Labeled] = null
  val nbclass:Int = Node.nbclass

  def printParams() : String = {
    val str_param:String = "max_features=" + max_features + ";" +
    "max_depth=" + max_depth + ";" +
    "min_samples_split=" + min_samples_split + ";"
    str_param
  }

  def isLeaf() = {
    if (left == null && right == null) {
      true
    } else {
      false
    }
  }

  def canSplit(): Boolean = {
    if (max_depth > 0) {
      if (samples.length > min_samples_split && depth <= max_depth && split.attribute != -1) {
        true
      } else {
        false
      }
    } else {
      if (samples.length > min_samples_split && split.attribute != -1) {
        true
      } else {
        false
      }
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

  def findRandomSplit(): Split = {
    val rand = new Random
    var att = Random.nextInt(samples(0).input.length)
    var th = -1.0
    var att_vector = rand.shuffle(samples.map(i => i.input(att)))
    att_vector = att_vector.distinct
    if (att_vector.length > 1) {
      th = math.min(att_vector(0),att_vector(1)) + (math.abs(att_vector(0) - att_vector(1)) / 2.0)
    } else {
      att = -1
    }
    Split(att,th)
  }

  def fit(): Unit = {
    split = findRandomSplit()
    setVotes()
    if (canSplit()) {
      val partitions = samples.partition(i => i.input(split.attribute) < split.threshold)
      left = new Node(max_features,max_depth,min_samples_split)
      left.depth = depth + 1
      left.samples = partitions._1
      left.fit()
      right = new Node(max_features,max_depth,min_samples_split)
      right.depth = depth + 1
      right.samples = partitions._2
      right.fit()
    }
  }

  def setVotes() = {
    val maps = samples.groupBy(e => e.label.label)
    val counts = maps.map(e => { (e._1, e._2.length) } )
    votes = new Array[Double](nbclass)
    var total:Double = samples.length
    for (e <- counts) {
      votes(e._1) = e._2/total
    }
  }

  def predict(x: Unlabeled): Array[Double] = {
    if (!isLeaf) {
      // propagate until it reaches a leaf.
      if (x.input(split.attribute) < split.threshold) {
          left.predict(x)
        } else {
          right.predict(x)
        }
    } else {
      votes
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
