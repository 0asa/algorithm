package io.datalayer.randomforest


import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed.{IndexedRow, IndexedRowMatrix}
import org.apache.spark.mllib.linalg.distributed.DistributedMatrix
import org.apache.spark.rdd._
import Math.pow

/*
  A few temporary classes to handle data...
*/
case class Label(label: Int)
case class Labeled(input: Seq[Float], label: Label)
case class Unlabeled(input: Seq[Float])

case class IncompatibleDataTypeException(message: String) extends Exception(message)

trait DataDNA{
  type data_type
  type TX
  type TY

  var labeled:Boolean = false
  var nb_attributes:Int = 0
  var nb_objects:Int = 0
  var nb_classes:Int = 0
  var inputs:TX
  var labels:TY

  def load(X:TX, Y:TY)
  def loadCSV(uri: String, label: Int)

  def split(attr: Int, thr: Double): (DataDNA, DataDNA)

  def getObject(index : Int) : DataDNA = { getObjects(Traversable(index)) }
  def getObjects(indexes : Traversable[Int]) : DataDNA

  def getAttribute(index : Int) : DataDNA = { getAttributes(Traversable(index)) }
  def getAttributes(indexes : Traversable[Int]) : DataDNA

  def getLabel(index : Int) : TY = { getLabels(Traversable(index)) }
  def getLabels(indexes : Traversable[Int]) : TY

  def getCounts(): Map[data_type,Int]

  def getValue(i: Int, j: Int) : data_type

  def describe
}

class Data extends DataDNA {
  type data_type = Double
  type TX = Seq[Seq[data_type]]
  type TY = Seq[data_type]
  var inputs: Seq[Seq[data_type]] = Seq.empty
  var labels: Seq[data_type] = Seq.empty

  def load(X: TX, Y: TY = Seq.empty) {
    // TODO: add some check somewhere

    inputs = X
    nb_objects = X.length
    nb_attributes = X(0).length

    // Check if we have a labels
    if (!Y.isEmpty) {
      if (X.length != Y.length) throw IncompatibleDataTypeException(s"Inputs and labels have different size: ${X.length} != ${Y.length}")
      labeled = true
      labels = Y
      nb_classes = labels.distinct.length
    } else {
      labeled = false
      labels = Seq.empty
    }
  }

  def loadCSV(uri: String, label: Int) = { println("Data loadCSV") }

  def split(attr: Int, thr: Double): (Data, Data) = {
    val partOne = new Data
    val partTwo = new Data
    if (labeled) {
      val zipped = inputs.zip(labels).partition(i => i._1(attr) < thr)
      partOne.load(zipped._1.unzip._1, zipped._1.unzip._2)
      partTwo.load(zipped._2.unzip._1, zipped._2.unzip._2)
    } else {
      val splitted = inputs.partition(_(attr) < thr)
      partOne.load(splitted._1)
      partTwo.load(splitted._2)
    }
    (partOne, partTwo)
  }

  def getObjects(indexes : Traversable[Int]) : Data = {
    val newData = new Data
    newData.load(indexes.map{i => inputs(i)}.toSeq,
      labels.zipWithIndex.filter(x => indexes.exists(_ == x._2)).map(_._1) )
    newData
  }


  def getAttributes(indexes : Traversable[Int]) : Data = {
    val newData = new Data
    val newInputs = inputs.map(in => in.zipWithIndex.filter(x => indexes.exists(_ == x._2)).map(x => x._1) )
    newData.load(newInputs, labels)
    newData
  }

  def getLabels(indexes : Traversable[Int]) : TY = {
    indexes.map{i => labels(i)}.toSeq
  }
  def getValue(i: Int, j: Int) : data_type = { inputs(i)(j) }

  def getCounts() : Map[data_type,Int] = {
    val maps = labels.groupBy(e => e)
    maps.map(e => { (e._1, e._2.length) } )
  }

  def describe {
    if (inputs.isEmpty) {
      println("There is no data.")
      return
    }

    println("\nData description :")
    println("------------------")
    println("Is there labels ?: " + labeled)
    println("Number of objects: " + inputs.length)
    println("Number of attributes: " + inputs(0).length)

    for (i <- 0 to (inputs(0).length - 1) ) {
      println("Attribute " + i + " :")
      val mean = inputs.map(_(i)).reduce(_ + _) / inputs.length
      println("Mean: " + mean)
      println("Sample Variance: " + inputs.map( (x:Seq[Double]) => Math.pow(x(i) - mean, 2)).sum /(inputs.length - 1) )
    }
  }

}

//class DataRDD(sc: SparkContext) extends DataDNA {
//  type data_type = Double
//  type TX = IndexedRowMatrix
//  type TY = RDD[data_type]
//  var inputs: TX = new IndexedRowMatrix(sc.parallelize(Array(IndexedRow(0,Vectors.dense(Array[data_type](0.0))))))
//  var labels: TY = sc.emptyRDD[data_type]
//
//  def load(X: TX, Y: TY = sc.emptyRDD[data_type]) {
//    // TODO: add some check somewhere
//    // such as X.length == Y.length
//
//    inputs = X
//    nb_objects = X.numRows().toInt
//    nb_attributes = X.numCols().toInt
//
//    // Check if we have a labels
//    if (Y.count > 0) {
//      labeled = true
//      labels = Y
//      nb_classes = labels.distinct.count.toInt
//    } else {
//      labeled = false
//      labels = sc.emptyRDD[data_type]
//    }
//  }
//
//  def loadCSV(uri: String, label: Int) = { println("Data loadCSV") }
//
//  def split(attr: Int, thr: Double): (DataRDD, DataRDD) = {
////    val partOne = new Data
////    val partTwo = new Data
////    val zipped = inputs.zip(labels).partition(i => i._1(attr) < thr)
////    partOne.load(zipped._1.unzip._1, zipped._1.unzip._2)
////    partTwo.load(zipped._2.unzip._1, zipped._2.unzip._2)
////    (partOne, partTwo)
//    (this, this)
//  }
//
//  def getObjects(indexes : Traversable[Int]) : TX = {
////    indexes.map{i => inputs(i)}.toSeq
//    inputs
//  }
//
//
//  def getAttributes(indexes : Traversable[Int]) : TX = {
////    (for (i <- indexes) yield { inputs.map(_(i)) }).toSeq
//  inputs
//  }
//
//  def getLabels(indexes : Traversable[Int]) : TY = {
////    indexes.map{i => labels(i)}.toSeq
//    labels
//  }
//
//  def getValue(i: Int, j: Int) : data_type = { 0.0 }
//
//  def getCounts() : Map[data_type,Int] = {
//    // TODO
//    null
//  }
//
//  def describe {
////    if (inputs.isEmpty) {
////      println("There is no data.")
////      return
////    }
//
////    println("\nData description :")
////    println("------------------")
////    println("Is there labels ?: " + labeled)
////    println("Number of objects: " + inputs.length)
////    println("Number of attributes: " + inputs(0).length)
////
////    for (i <- 0 to (inputs(0).length - 1) ) {
////      println("Attribute " + i + " :")
////      val mean = inputs.map(_(i)).reduce(_ + _) / inputs.length
////      println("Mean: " + mean)
////      println("Sample Variance: " + inputs.map( (x:Seq[Double]) => Math.pow(x(i) - mean, 2)).sum /(inputs.length - 1) )
////    }
//  }
//}

// Helper to generate dummy data
// for testing purposes
object dataGenerator {
  /*
  def genLabeledPoint(sc: SparkContext, numInstances: Int = 10) = {
  val arr = new Array[LabeledPoint](numInstances)
  val rand = new Random
  for(i <- 0 until numInstances){
  val x = rand.nextInt(10)
  val y = rand.nextInt(10)
  val label = if (x+y > 10) { 0 } else { 1 }
  arr(i) = new LabeledPoint(label, Vectors.dense(x,y))
}
sc.parallelize(arr)
}
*/
/*
def genIndexedRowMatrix(sc: SparkContext, numInstances: Int = 10) = {
val arr = new Array[IndexedRow](numInstances)
val rand = new Random
for(i <- 0 until numInstances){
val x = rand.nextInt(10)
val y = rand.nextInt(10)
val label = if (x+y > 10) { 0 } else { 1 }
arr(i) = new IndexedRow(i, Vectors.dense(x,y,label))
}
val rdd = sc.parallelize(arr)
val mat: IndexedRowMatrix = new IndexedRowMatrix(rdd)
mat
}
*/
/*
def genArray(numInstances: Int = 10): (DenseMatrix[Double], DenseVector[Double]) = {
val x = DenseMatrix.zeros[Double](numInstances,2)
val y = DenseVector.zeros[Double](numInstances)
val rand = new Random
for (i:Int <- 0 until numInstances) {
val a = rand.nextInt(10)
val b = rand.nextInt(10)
x(i, 0) = a
x(i, 1) = b
y(i) = if (a + b > 10) { 0 } else { 1 }
}
(x, y)
}
*/

def genData(numInstances: Int = 10, numFeatures: Int = 10, labeled: Boolean = false): Data = {
  val rand = new scala.util.Random
  val x = new Data
  var labels = Seq.empty[Double]
  val inputs: Seq[Seq[Double]] = for (i:Int <- 0 until numInstances) yield {
    val a:Double = rand.nextInt(10)
    val b:Double = rand.nextInt(10)
    val c:Double = rand.nextInt(100)
    val d:Seq[Double] = Seq.fill(math.max(3,numFeatures) - 3)(rand.nextFloat())
    val y:Double = if ((a+b) > 0 && (a+b) < 6) { 0 } else if ((a+b) >= 6 && (a+b) < 12) { 1 } else { 2 }
    labels = labels :+ y
    Seq(a,b,c)++d
  }
  if (labeled)
    x.load(inputs,labels)
  else
    x.load(inputs)
  x
}

def genDataRDD(numInstances: Int = 10, numFeatures: Int = 10,
               labeled: Boolean = false, sc: SparkContext): DataSchemaRDD = {
  val data = genData(numInstances, numFeatures, labeled)
  val x = new DataSchemaRDD(sc)
  val in = sc.parallelize(data.inputs.map(_.toArray))
  if (labeled)
    x.load(in, sc.parallelize(data.labels.zipWithIndex.map(x => (x._1, x._2.toLong))))
  else
    x.load(in)

  x
}
def genLabeled(numInstances: Int = 10, numFeatures: Int = 10): Seq[Labeled] = {
  val rand = new scala.util.Random
  val x: Seq[Labeled] = for (i:Int <- 0 until numInstances) yield {
    val a:Float = rand.nextInt(10)
    val b:Float = rand.nextInt(10)
    val c:Float = rand.nextInt(100)
    val d = Seq.fill(math.max(3,numFeatures) - 3)(rand.nextFloat())
    val y = if ((a+b) > 0 && (a+b) < 6) { 0 } else if ((a+b) >= 6 && (a+b) < 12) { 1 } else { 2 }
    Labeled(Seq(a,b,c)++d, Label(y))
  }
  x
}

def genUnlabeled(numInstances: Int = 10, numFeatures: Int = 10): Seq[Unlabeled] = {
  val rand = new scala.util.Random
  val x: Seq[Unlabeled] = for (i:Int <- 0 until numInstances) yield {
    val a:Float = rand.nextInt(10)
    val b:Float = rand.nextInt(10)
    val c:Float = rand.nextInt(100)
    val d = Seq.fill(math.max(3,numFeatures) - 3)(rand.nextFloat())
    Unlabeled(Seq(a,b,c)++d)
  }
  x
}
}
