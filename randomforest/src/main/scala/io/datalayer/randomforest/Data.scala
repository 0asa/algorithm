package io.datalayer.randomforest

case class Label(label: Int)
case class Labeled(input: Seq[Float], label: Label)
case class Unlabeled(input: Seq[Float])