package io.datalayer.randomforest

trait Learner {
  private def getMaxByIndex(x: Array[Double]): Label = Label(x.zipWithIndex.maxBy(_._1)._2)

  def fit(x: Seq[Labeled])
  
  def predict(x: Unlabeled): Array[Double]
  def predict(x: Seq[Unlabeled]): Array[Array[Double]]

  // @manu here what you should do
  // 1. Seperate labels from inputs
  // 2. Call predictLabel
  // 3. Call accuracy(predictions, labels)
  //def predictEval(x: Labeled): Array[Double]
  //def predictEval(x: Seq[Labeled]): Array[Array[Double]]

  def predictLabel(x: Unlabeled): Label = getMaxByIndex(predict(x))
  def predictLabel(x: Array[Double]): Label = getMaxByIndex(x)
  def predictLabel(x: Seq[Unlabeled]): Seq[Label] = predict(x).map(getMaxByIndex(_))
  def predictLabel(x: Array[Array[Double]]): Seq[Label] = x.map(getMaxByIndex(_))

  def predictEval(x: Seq[Labeled]): Array[Array[Double]] = {
    val unlabeledSeq = x.map{l: Labeled => Unlabeled(l.input) }
    val out = predict(unlabeledSeq)
    val prediction = predictLabel(out)
    val accuracy = Metrics.accuracy(prediction, x.map(_.label))
    println("Accuracy:")
    println(accuracy)
    println("Error rate:")
    println(1 - accuracy)

    out
  }
}