package io.datalayer.randomforest

/** A split case class
  *
  * @constructor create a new split for node expansion.
  * @param attribute the attribute index being tested
  * @param threshold the threshold use for the split
  * @param score the resulting score
  * @param size the number of samples used
  */
case class Split(
	attribute: Int = -1,
	threshold: Double = -1,
	var score: Double = -1,
	var size: Int = 0)
