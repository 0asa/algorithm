package io.datalayer.randomforest

trait Learner {
  private def getMaxByIndex(x: Array[Double]): Label = Label(x.zipWithIndex.maxBy(_._1)._2)

  def fit(x: Seq[Labeled])
  def predict(x: Unlabeled): Array[Double]
  def predict(x: Seq[Unlabeled]): Array[Array[Double]]
  def predictLabel(x: Unlabeled): Label = getMaxByIndex(predict(x))
  def predictLabel(x: Seq[Unlabeled]): Seq[Label] = predict(x).map(getMaxByIndex(_))
}