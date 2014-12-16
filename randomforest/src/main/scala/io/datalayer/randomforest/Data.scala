package io.datalayer.randomforest

/*
  A few temporary classes to handle data...
*/
case class Label(label: Int)
case class Labeled(input: Seq[Float], label: Label)
case class Unlabeled(input: Seq[Float])
