package io.datalayer.randomforest

/*
  Split class
*/
case class Split(
	attribute: Int = -1, 
	threshold: Double = -1, 
	var score: Double = -1, 
	var size: Int = 0) 
