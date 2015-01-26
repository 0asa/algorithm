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

  /** Method desc.
    *
    * @param
    * @return
    */
  def isLeaf(): Boolean = {
    if (left == null && right == null) return true
    return false
  }

  /** Method desc.
    *
    * @param
    * @return
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

  /** Method desc.
    *
    * @param
    * @return
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

  /** Method desc.
    *
    * @param
    * @return
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

  /** Method desc.
    *
    * @param
    * @return
    */
  def giniScore(p: Seq[Labeled], pl: Seq[Labeled], pr: Seq[Labeled]) : Double = {
    val gs: Double = p.length*gini(p) - (pl.length*gini(pl) + pr.length*gini(pr))
    gs/p.length
  }

  /** Method desc.
    *
    * @param
    * @return
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

  /** Method desc.
    *
    * @param
    * @return
    */
  def infogainScore(p: Seq[Labeled], pl: Seq[Labeled], pr: Seq[Labeled]) : Double = {
    val is: Double = p.length*infogain(p) - (pl.length*infogain(pl) + pr.length*infogain(pr))
    is/p.length
  }

  /** Method desc.
    *
    * @param
    * @return
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

  /** Method desc.
    *
    * @param
    * @return
    */
  def updateSplitScore(s: Split, scorer:(Seq[Labeled], Seq[Labeled], Seq[Labeled]) => Double = infogainScore) {
    val part = samples.partition(i => i.input(s.attribute) < s.threshold)
    s.score = scorer(samples,part._1,part._2)
    s.size = samples.length
  }

  /** Method desc.
    *
    * @param
    * @return
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

  /** Method desc.
    *
    * @param
    * @return
    */
  def _findRandomSplit(): Split = {
    _samples.findRandomSplit
  }

  /** Method desc.
    *
    * @param
    * @return
    */
  def _fit(): Unit = {
    // TODO: use DataDNA _ methods
    split = _findRandomSplit()
    _setVotes()
  }

  /** Method desc.
    *
    * @param
    * @return
    */
  def fit(): Unit = {
    split = findKRandomSplit()
    setVotes()
  }

  /** Method desc.
    *
    * @param
    * @return
    */
  def _setVotes() = {
    val counts = _samples.getCounts
    votes = new Array[Double](nbclass)
    var total:Double = _samples.nb_objects
    for (e <- counts) {
      votes(e._1.asInstanceOf[Double].toInt) = e._2/total
    }
  }

  /** Method desc.
    *
    * @param
    * @return
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

  /** Method desc.
    *
    * @param
    * @return
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

  /** Method desc.
    *
    * @param
    * @return
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

  /** Method desc.
    *
    * @param
    * @return
    */
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
