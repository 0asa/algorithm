package io.datalayer.randomforest

import scala.collection.mutable
import scala.util.Random

/*
  Companion object
*/
object Node {
  var nbclass: Int = 0
}


/** A node for decision trees
  *
  * @constructor create a new node for decision tree induction.
  * @param max_features the number of features tested at each node
  * @param max_depth the maximum depth of trees
  * @param min_samples_split the number of samples required in a splitting node
  */
class Node( max_features: Int = 10,
            max_depth: Int = -1,
            min_samples_split: Int = 2) {

  var left: Node = null
  var right: Node = null
  var split: Split = null
  var depth: Int = 1
  var votes: Array[Double] = null
  var samples: Seq[Labeled] = null
  var _samples: DataDNA[Double,Seq[Double],Int] = null
  val nbclass:Int = Node.nbclass

  override def toString: String = {
    val str_param:String = "max_features=" + max_features + ";" +
    "max_depth=" + max_depth + ";" +
    "min_samples_split=" + min_samples_split + ";"
    str_param
  }

  /** Check if a node is a leaf
    *
    * @return true if node is a leaf, false otherwise
    */
  def isLeaf(): Boolean = {
    if (left == null && right == null) return true
    return false
  }

  /** Check if a node can be a splitting node
    *
    * NOTE: use DataDNA
    *
    * @return true is a node can split, false otherwise
    */
  def _canSplit(): Boolean = {
    if (max_depth > 0) {
      if (_samples.nb_objects > math.max(2,min_samples_split) && depth <= max_depth && split.attribute != -1) {
        return true
      }
      return false
    } else {
      if (_samples.nb_objects > math.max(2,min_samples_split) && split.attribute != -1) {
        return true
      }
      return false
    }
  }

  /** Check if a node can be a splitting node
    *
    * @return true is a node can split, false otherwise
    */
  def canSplit(): Boolean = {
    if (max_depth > 0) {
      if (samples.length > math.max(2,min_samples_split) && depth <= max_depth && split.attribute != -1) {
        return true
      }
      return false
    } else {
      if (samples.length > math.max(2,min_samples_split) && split.attribute != -1) {
        return true
      }
      return false
    }
  }

  /** Compute the Gini index for a sequence of labeled samples
    *
    * @param p a partition of labeled objects
    * @return gi the gini index
    */
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

  /** Compute the Gini score for 3 partitions of labeled samples
    *
    * @param p the partition at the current node
    * @param pl the partition found in the left child node
    * @param pr the partition found in the right child node
    * @return gs the Gini score
    */
  def giniScore(p: Seq[Labeled], pl: Seq[Labeled], pr: Seq[Labeled]) : Double = {
    val gs: Double = p.length*gini(p) - (pl.length*gini(pl) + pr.length*gini(pr))
    gs/p.length
  }

  /** Compute the information gain for a sequence of labeled samples
    *
    * @param p a partition of labeled objects
    * @return gi the information gain
    */
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

  /** Compute the information gain score for 3 partitions of labeled samples
    *
    * @param p the partition at the current node
    * @param pl the partition found in the left child node
    * @param pr the partition found in the right child node
    * @return gs the information gain score
    */
  def infogainScore(p: Seq[Labeled], pl: Seq[Labeled], pr: Seq[Labeled]) : Double = {
    val is: Double = p.length*infogain(p) - (pl.length*infogain(pl) + pr.length*infogain(pr))
    is/p.length
  }

  /** Search for the best split among max_features random splits
    *
    * @return split the best split
    */
  def findKRandomSplit(): Split = {
    val score_max:Double = -1.0
    var ksplits:Vector[Split] = Vector.empty
    var s = Split()
    for (i <- 0 until math.min(max_features,samples(0).input.length)) {
      //println(i)
      s = findRandomSplit()
      if (s.attribute != -1)
        updateSplitScore(s)
      ksplits = ksplits :+ s
    }
    ksplits.maxBy(_.score)
  }

  /** Update the split information
    *
    * @param s a give split
    * @param scorer a function to compute a score (default to infogainScore)
    * @return nothing
    */
  private def updateSplitScore(s: Split, scorer:(Seq[Labeled], Seq[Labeled], Seq[Labeled]) => Double = infogainScore) {
    val part = samples.partition(i => i.input(s.attribute) < s.threshold)
    s.score = scorer(samples,part._1,part._2)
    s.size = samples.length
  }

  /** Find a random split (an attribute and a valid threshold)
    *
    * TODO: this is a rather naive implementation
    *
    * @return split a split
    */
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

  /** Find a random split (an attribute and a valid threshold)
    *
    * NOTE: use DataDNA
    *
    * @return split a split
    */
  def _findRandomSplit(): Split = {
    _samples.findRandomSplit
  }

  /** Expand the current node.
    *
    * NOTE: use DataDNA
    *
    * @return nothing
    */
  def _fit(): Unit = {
    // TODO: use DataDNA _ methods
    split = _findRandomSplit()
    _setVotes()
  }

  /** Expand the current node.
    *
    * @return nothing
    */
  def fit(): Unit = {
    split = findKRandomSplit()
    setVotes()
  }

  /** Store/update the vector of votes.
    *
    * votes are vector of class probabilities (frequencies)
    *
    * NOTE: use DataDNA
    *
    * @return nothing
    */
  def _setVotes() = {
    val counts = _samples.getCounts
    votes = new Array[Double](nbclass)
    var total:Double = _samples.nb_objects
    for (e <- counts) {
      votes(e._1.asInstanceOf[Double].toInt) = e._2/total
    }
  }

  /** Store/update the vector of votes.
    *
    * votes are vector of class probabilities (frequencies)
    *
    * @return nothing
    */
  def setVotes() = {
    val maps = samples.groupBy(e => e.label.label)
    val counts = maps.map(e => { (e._1, e._2.length) } )
    votes = new Array[Double](nbclass)
    var total:Double = samples.length
    for (e <- counts) {
      votes(e._1) = e._2/total
    }
  }

  /** Predict method for a single Unlabeled.
    *
    * Propagate object until it reaches a leaf.
    *
    * @param x an Unlabeled object
    * @return votes the vector of class probabilities
    */
  def predict(x: Unlabeled): Array[Double] = {
    if (!isLeaf) {
      if (x.input(split.attribute) < split.threshold) {
          left.predict(x)
        } else {
          right.predict(x)
        }
    } else {
      votes
    }
  }

  /** Predict method for a single row of DataDNA.
    *
    * Propagate object until it reaches a leaf.
    *
    * TODO: abstract types contained in RowDNA
    *
    * @param x a row of dataDNA
    * @return votes the vector of class probabilities
    */
  def predict(x: RowDNA[Double,Seq[Double], Int]): Array[Double] = {
    if (!isLeaf) {
      if (x.attributes(split.attribute) < split.threshold) {
          left.predict(x)
        } else {
          right.predict(x)
        }
    } else {
        votes
    }
  }

  def display() {
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
        println(votes.mkString(" | "))
      }
    }
  }
}
