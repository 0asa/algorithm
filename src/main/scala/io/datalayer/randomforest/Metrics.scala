package io.datalayer.randomforest

// Helper to compute various metrics...
// will be extend later...
object Metrics {
  def accuracy(pred: Seq[Label], labels: Seq[Label]): Double = {
    pred.zip(labels).filter(x => x._1 == x._2).size.toDouble / pred.length
  }

  def auc() = {}
  def entropy() = {}
  def gini() = {}
  def variance() = {}
}
