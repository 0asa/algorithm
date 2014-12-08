package io.datalayer.randomforest

import io.datalayer.randomforest._
import breeze.linalg._
import org.scalatest.FunSuite
import org.scalatest.ShouldMatchers

class NodeTest extends FunSuite with ShouldMatchers {
  val (x, y) = dataGenerator.genArray(40)

  test("Node is a leaf") {    
    val node = new Node    
    assert(node.isLeaf == true)
  }

  test("Node.findRandomSplit") {
    val node = new Node
    node.findRandomSplit(x,y)
    assert(1 === 1)
  }
  
}

class TreeTest extends FunSuite with ShouldMatchers {
  test("Some tree test") {    
    val tree = new Tree
    assert(1 === 1)
  }
}


class ForestTest extends FunSuite with ShouldMatchers {
  test("Some forest test") {    
    val forest = new Forest
    assert(1 === 1)
  }
}

class MainTest extends FunSuite with ShouldMatchers {
  test("Some more test to test scala") {    
    
    var dv = DenseVector.rand(10)
    val part = dv.toArray.partition(ex => ex < 0.5)
    println("part._1")
    part._1.foreach(println)
    println("part._2")
    part._2.foreach(println)
    
    assert(1 === 1)
  }
}
