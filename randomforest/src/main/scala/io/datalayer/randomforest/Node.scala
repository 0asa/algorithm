package io.datalayer.randomforest

import scala.collection.mutable

// Node class: currently, I'm trying to keep things
// consistent with a fit and predict
// while it has a different meaning at the node level
// in a classic decision tree, it could make sense
// if we extend this to do more "complicated" stuff
class Node {
  // TODO: add left and right child
  // TODO: set a split type (at random, best split, ...)
  def isLeaf() = { true }
  def fit(x: Array[Array[Double]], y: Array[Double]) = {}
  def predict(x: Array[Array[Double]]) = {}
}