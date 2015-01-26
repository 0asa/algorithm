package io.datalayer.randomforest

import io.datalayer.randomforest._
import breeze.linalg._
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed.{IndexedRow, IndexedRowMatrix}
import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

//import org.scalatest.ShouldMatchers

//import org.apache.spark.SparkContext
//import org.apache.spark.SparkContext._
//import org.apache.spark.SparkConf
import scala.language.implicitConversions
import io.datalayer.common.SparkContextManager

object TestParams {
  val ls_size = 300
  val ts_size = 100
  val train = dataGenerator.genLabeled(ls_size,50)
  val test = dataGenerator.genUnlabeled(ts_size,50)
  val evaluate = dataGenerator.genLabeled(ts_size,50)
}

class ForestTest extends FunSuite {

  val train = TestParams.train
  val test = TestParams.test
  val evaluate = TestParams.evaluate

  test("Forest fit on DataDNA") {
    val trees = new Forest(min_samples_split=10,n_estimators=10,max_features=5)
    val unlabeled = dataGenerator.genData(50,10,false)
    assert(unlabeled.labeled == false)
    intercept[CannotFitException]{
      trees.fit(unlabeled)
    }

    val labeled = dataGenerator.genData(50,10,true)
    assert(labeled.labeled == true)
    trees.fit(labeled)

    val pred = trees.predict(labeled)
    //pred.foreach(p => println(p.mkString("|")))
  }

  test("Forest fit on DataRDD") {
    info("Nothing done yet here")
    //val sc = SparkContextManager.getSparkContext(8)
    //val trees = new Forest(min_samples_split=10,n_estimators=100,max_features=25)
    //val unlabeled = dataGenerator.genDataRDD(50,50,false,sc)

    // TODO: Make it work !
//    intercept[CannotFitException]{
//      trees.fit(unlabeled)
//    }
//
//    val labeled = dataGenerator.genDataRDD(50,50,true,sc)
//    trees.fit(labeled)

  }

  test("Totally Random Trees vs. Extra-Trees") {
    val random_trees = new Forest(min_samples_split=10,n_estimators=100,max_features=1)
    info(random_trees)
    random_trees.fit(train)
    val acc_random = random_trees.predictEval(evaluate)._2
    val extra_trees = new Forest(min_samples_split=10,n_estimators=100,max_features=25)
    info(extra_trees)
    extra_trees.fit(train)
    val acc_extra = extra_trees.predictEval(evaluate)._2
    info("Totally Random acc.: " + acc_random)
    info("Extra-Trees acc.: " + acc_extra)
    assert(acc_random < acc_extra)
  }

  test("Single tree vs. Extra-Trees") {
    val tree = new Tree(min_samples_split=10,max_features=25)
    tree.fit(train)
    info(tree)
    val acc_tree = tree.predictEval(evaluate)._2
    val extra_trees = new Forest(min_samples_split=10,n_estimators=100,max_features=25)
    extra_trees.fit(train)
    info(extra_trees)
    val acc_extra = extra_trees.predictEval(evaluate)._2
    info("Single tree acc.: " + acc_tree)
    info("Extra-Trees acc.: " + acc_extra)
    assert(acc_tree < acc_extra)
  }

  test("Some forest test") {
    val forest = new Forest(min_samples_split=10,n_estimators=50,max_features=25)
    info(forest)
    forest.fit(train)
    val accuracy = forest.predictEval(evaluate)._2
    info("Accuracy = " + accuracy)
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

class SparkTest extends FunSuite {
  test("Creating Spark Context") {
    //val conf = new SparkConf().setMaster("local[4]").setAppName("Simple Application")
    val sc = SparkContextManager.getSparkContext(8)
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
//    sc.stop()
    assert(1 == 1)
  }
}
