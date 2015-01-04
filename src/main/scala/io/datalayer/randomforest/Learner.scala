package io.datalayer.randomforest

/*
 Learner trait: will evolve and end up in the common folder
*/
trait Learner {
  private def getMaxByIndex(x: Array[Double]): Label = Label(x.zipWithIndex.maxBy(_._1)._2)

  def fit(x: Seq[Labeled])

  def predict(x: Unlabeled): Array[Double]
  def predict(x: Seq[Unlabeled]): Array[Array[Double]]

  def predictLabel(x: Unlabeled): Label = getMaxByIndex(predict(x))
  def predictLabel(x: Array[Double]): Label = getMaxByIndex(x)
  def predictLabel(x: Seq[Unlabeled]): Seq[Label] = predict(x).map(getMaxByIndex(_))
  def predictLabel(x: Array[Array[Double]]): Seq[Label] = x.map(getMaxByIndex(_))

  def predictEval(x: Seq[Labeled]): (Array[Array[Double]], Double) = {
    val unlabeledSeq = x.map{l: Labeled => Unlabeled(l.input) }
    val out = predict(unlabeledSeq)
    val prediction = predictLabel(out)
    val accuracy = Metrics.accuracy(prediction, x.map(_.label))
    (out, accuracy)
  }
}
