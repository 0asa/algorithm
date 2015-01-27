package io.datalayer.randomforest

/** Helper to compute various metrics 
  *	(such as accuracy, auc, log loss, confusion matrix, ...)
  *
  * TODO: Should be extended later.
  */
object Metrics {
  def accuracy(pred: Seq[Label], labels: Seq[Label]): Double = {
    pred.zip(labels).filter(x => x._1 == x._2).size.toDouble / pred.length
  }

  def auc(): Double = { 0.0 }
  def entropy() = {}
  def gini() = {}
  def variance() = {}
}
