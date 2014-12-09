package io.datalayer.randomforest

//import breeze.linalg._

trait Learner {
	def fit(x: Seq[Labeled])
	def predict(x: Seq[Unlabeled]): Seq[Double]
}