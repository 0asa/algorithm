package io.datalayer.randomforest

import io.datalayer.randomforest._
import breeze.linalg._
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed.{IndexedRow, IndexedRowMatrix}
import org.scalatest.FunSuite
//import org.scalatest.ShouldMatchers

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import scala.language.implicitConversions

object TestParams {
  val ls_size = 500
  val ts_size = 1000
  val train = dataGenerator.genLabeled(ls_size,50)
  val test = dataGenerator.genUnlabeled(ts_size,50)
  val evaluate = dataGenerator.genLabeled(ts_size,50)
}

class SparkTest extends FunSuite {
  test("Creating Spark Context") {
    val conf = new SparkConf().setMaster("local[4]").setAppName("Simple Application")
    val sc = new SparkContext(conf)
    Thread sleep 5000
    val rdd = sc.parallelize(TestParams.train)
    Thread sleep 1000
    rdd.foreach(println)
    Thread sleep 1000
    val coucou = rdd.map(i => i.input(1)*10)
    Thread sleep 1000
    coucou.foreach(println)
    println("You now have 20 seconds left...")
    Thread sleep 20000
    println("Done. Byebye.")
    sc.stop()
    assert(1 == 1)
  }
}

class DataTest extends FunSuite {

  test("Input and labels with different sizes") {
    val data = new Data
    intercept[IncompatibleDataTypeException]{
      data.load(Seq(Seq(1.0,1.1),Seq(2.0,2.1),Seq(3.0,3.1)), Seq(0.0,0.1))
    }
  }

  test("First DataDNA test") {
    info("Going to be the coolest thing you've ever done!")
    val data = new Data
    data.load(Seq(Seq(1.0,1.1),Seq(2.0,2.1),Seq(3.0,3.1)))
    data.split(0, 1.5)
    data.load(Seq(Seq(1.0,1.1),Seq(2.0,2.1),Seq(3.0,3.1)), Seq(0.0,0.1,0.2))
    data.inputs.foreach(println)
    println(data.labels)

    //data.loadCSV

    // Test split
    val splited = data.split(0, 1.5)
    println(splited._1.labels)
    println(splited._2.labels)

    data.describe

    assert(splited._1.labels == Seq(0.0))

    assert(data.getAttribute(0) == Seq(Seq(1.0,2.0,3.0)))
    assert(data.getAttributes(Seq(0,1)) == Seq(Seq(1.0,2.0,3.0),Seq(1.1,2.1,3.1)))

    assert(data.getObjects(Seq(0,1)) == Seq(Seq(1.0,1.1),Seq(2.0,2.1)))
    assert(data.getObject(1) == Seq(Seq(2.0,2.1)))

    assert(data.getLabels(Seq(0,1)) == Seq(0.0,0.1))
    assert(data.getLabel(1) == Seq(0.1))

    assert(data.getValue(0,0) == 1.0)
    assert(data.labeled == true)
    assert(data.nb_attributes == 2)
    assert(data.nb_objects == 3)
  }

  test("DataRDD test") {
    info("Some serious stuff going on…")
    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("DataSchemaRDD Application")
    val sc = new SparkContext(sparkConf)
    val data = new DataSchemaRDD(sc)
    val train = sc.parallelize(Seq(Array(1.0,1.1), Array(2.0,2.1), Array(3.0,3.1)))
    val labels = sc.parallelize(Seq((1.0, 1.toLong), (2.0, 1.toLong), (1.0, 0.toLong)))

    data.load(train, labels)
    val split = data.split(0, 1.5)
    println(split._1.inputs.take(1).take(1)(0)(0))
    println(split._2.inputs.take(1).take(1)(0)(0))
    assert(data.inputs.take(1).take(1)(0)(1) === 1.1)

    //    data.loadCSV("/home/manuel/wrk/model/randomforest/src/test/resources/", 0)
  }
}

class NodeTest extends FunSuite {

  val train = TestParams.train
  val test = TestParams.test
  val maps = train.groupBy(e => e.label.label)
  Node.nbclass = maps.size

  test("Node should be a leaf") {
    val node = new Node
    info(node)
    assert(node.isLeaf == true)
  }

  test("Node split after fit should not be null") {
    val node = new Node
    node.samples = train
    node.fit()
    assert(node.split !== null)
  }

  test("Node canSplit() test") {
    // A case where canSplit() should return true
    val ntrue = new Node(min_samples_split=TestParams.ls_size-1)
    ntrue.samples = train
    ntrue.split = ntrue.findRandomSplit()
    info(ntrue)
    assert(ntrue.canSplit === true)
    // A case where canSplit() should return false
    val nfalse = new Node(min_samples_split=TestParams.ls_size+1)
    nfalse.samples = train
    nfalse.split = nfalse.findRandomSplit()
    info(nfalse)
    assert(nfalse.canSplit === false)
  }

  test("Node findRandomSplit should find a split") {
    // The truth is it might not...
    // in which case split.attribute will be equal to -1
    // this is caused by the current findRandomSplit implementation
    val node = new Node
    node.samples = train
    val split = node.findRandomSplit()
    assert(split.attribute >= -1 && split.attribute <= train(0).input.length)
  }

  test("Node findKRandomSplit") {
    val node = new Node(max_features=1)
    node.samples = train
    val split = node.findKRandomSplit()
    assert(true)
  }

}

class TreeTest extends FunSuite {

  val train = TestParams.train
  val test = TestParams.test
  val evaluate = TestParams.evaluate

  test("Some tree test") {
    val tree = new Tree(min_samples_split=10,max_features=25)
    info(tree)
    tree.fit(train)
    info(tree)
    val accuracy = tree.predictEval(evaluate)._2
    info("Accuracy = " + accuracy)
    info("Error rate = " + (1 - accuracy))
    //tree.display
    // predict for one sample
    var prob = tree.predict(test(0))

    // predict for many samples
    var proball = tree.predict(test)

    val expectedClass = tree.predictLabel(test(0))
    val expectedClasses = tree.predictLabel(test)

    assert(prob === proball(0))
    assert(expectedClass === expectedClasses(0))
  }
}


class ForestTest extends FunSuite {

  val train = TestParams.train
  val test = TestParams.test
  val evaluate = TestParams.evaluate

  test("Totally Random Trees vs. Extra-Trees") {
    val random_trees = new Forest(min_samples_split=10,n_estimators=100,max_features=1)
    info(random_trees)
    random_trees.fit(train)
    val acc_random = random_trees.predictEval(evaluate)._2
    val extra_trees = new Forest(min_samples_split=10,n_estimators=100,max_features=25)
    info(extra_trees)
    extra_trees.fit(train)
    val acc_extra = extra_trees.predictEval(evaluate)._2
    info("Totally Random acc: " + acc_random)
    info("Extra-Trees acc.: " + acc_extra)
    assert(acc_random < acc_extra)
  }

  test("Some forest test") {
    val forest = new Forest(min_samples_split=10,n_estimators=200,max_features=25)
    info(forest)
    forest.fit(train)
    val accuracy = forest.predictEval(evaluate)._2
    info("Accuracy = " + accuracy)
    info("Error rate = " + (1 - accuracy))
    //forest.display
    // predict for one sample
    var prob = forest.predict(test(0))

    // predict for many samples
    var proball = forest.predict(test)

    val expectedClass = forest.predictLabel(test(0))
    val expectedClasses = forest.predictLabel(test)

    assert(expectedClass === expectedClasses(0))
    assert(prob === proball(0))
  }
}

class MainTest extends FunSuite {
  test("Some more test to test scala") {
    // I sometimes use this to test things...
    assert(1 === 1)
  }
}
