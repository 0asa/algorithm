package io.datalayer.randomforest

import io.datalayer.randomforest._
import org.scalatest.FunSuite
import org.scalatest.ShouldMatchers

class NodeTest extends FunSuite with ShouldMatchers {
  test("Node is a leaf") {    
    val node = new Node    
    assert(node.isLeaf == true)
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