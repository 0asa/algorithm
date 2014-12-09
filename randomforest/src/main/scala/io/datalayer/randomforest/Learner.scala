package io.datalayer.randomforest

trait Learner {
	def fit(x: Seq[Labeled])
	def predict(x: Seq[Unlabeled]): Seq[Double]
}