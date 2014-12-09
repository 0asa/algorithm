package io.datalayer.randomforest

trait Learner {
	def fit(x: Seq[Labeled])
    def predict(x: Unlabeled): Array[Double]
	def predict(x: Seq[Unlabeled]): Array[Array[Double]]
}