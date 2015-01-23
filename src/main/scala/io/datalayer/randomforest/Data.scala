package io.datalayer.randomforest

import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed.{IndexedRow, IndexedRowMatrix}
import org.apache.spark.mllib.linalg.distributed.DistributedMatrix
import org.apache.spark.rdd._
import Math.pow
import scala.io.Source
import scala.util.Random
import scala.reflect.ClassTag

/*
  A few temporary classes to handle data...
*/
case class Label(label: Int)
case class Labeled(input: Seq[Float], label: Label)
case class Unlabeled(input: Seq[Float])

/*
class TraversableRDD[T: ClassTag](sc: SparkContext) extends RDD[T](sc, Nil) {
  
  def compute(split: org.apache.spark.Partition,context: org.apache.spark.TaskContext): Iterator[T] = { 
    throw new UnsupportedOperationException("empty RDD")
  }
  protected def getPartitions: Array[org.apache.spark.Partition] = {
    throw new UnsupportedOperationException("empty RDD")
  }
  
}

class DataR extends DataDNA2[Double, RDD[Double], Int] {

}
*/

class RowDNA[T,TX <: Traversable[T],TY] (val attributes:TX, val label:Option[TY] = None) {
  val isLabeled:Boolean = { label != None }
  val nb_attributes:Int = attributes.size
  override def toString = {
    "attributes: [" + attributes.mkString(", ") + "]\tlabel: " + label.getOrElse("(unlabeled)")
  }
}

trait DataDNA[T,TX <: Traversable[T],TY] extends Traversable[RowDNA[T,TX,TY]] {
  val rows:Traversable[RowDNA[T,TX,TY]]
  def nb_objects: Int = rows.size
  val nb_attributes: Int
  val nb_classes: Int
  def labeled: Boolean

  def foreach[U](f: RowDNA[T,TX,TY] => U): Unit = {
    rows.foreach(f)
  }
  override def toString = {
    rows.mkString("\n")
  }  

  def getObject(index : Int) : RowDNA[T,TX,TY] = { getObjects(Traversable(index)).head }
  def getObjects(indexes : Traversable[Int]) : DataDNA[T,TX,TY]

  def getAttribute(index : Int) : TX = { getAttributes(Traversable(index)).head }
  def getAttributes(indexes : Traversable[Int]) : Traversable[TX]  
  def getValue(i: Int, j: Int) : T
  
  def getLabel(index : Int) : TY = { getLabels(Traversable(index)).head }
  def getLabels(indexes : Traversable[Int]) : Traversable[TY]

  def getCounts(): Map[TY,Int]
  def split(att: Int, th: Double): (DataDNA[T,TX,TY], DataDNA[T,TX,TY])
  def findRandomSplit() : Split
  
  //def describe // Do we need this now?
}

class Data(val rows: Seq[RowDNA[Double,Seq[Double], Int]]) extends DataDNA[Double,Seq[Double],Int] {  

  val nb_attributes = if (check_attributes) { rows(0).nb_attributes } else { 0 /* or throw exception */ }
  val nb_classes = if (check_labels) { rows.map(x => x.label).distinct.size } else { 0 }
  def labeled = check_labels

  private def check_attributes = {
    rows.map(_.nb_attributes).distinct.size == 1
  }

  private def check_labels = {
    val lab = rows.map(_.isLabeled).distinct     
    if (lab.size == 1)
      lab.head == true
    else 
      false
  }
  
  // Create a Data2 from a CSV file
  def this(uri: String, labelpos: Int, delimiter:String = " ") = {
    // TODO: read from CSV
    // create Seq[RowDNA] from the file
    // call this(Seq[RowDNA])
    this(Seq.empty[RowDNA[Double,Seq[Double], Int]])
  }

  // Create an empty Data2
  def this() = {
    this(Seq.empty[RowDNA[Double,Seq[Double], Int]])
  }

  def getAttributes(indexes : Traversable[Int]) = {
    indexes.map(
      i => rows.map(
        row => row.attributes(i)
        )
      )        
  }

  def getObjects(indexes : Traversable[Int]) = {
    new Data(indexes.map(i => rows(i)).toSeq)    
  }

  def getLabels(indexes : Traversable[Int]) = {
    indexes.map(i => rows(i).label.getOrElse(-1))
  }
  
  def getValue(row: Int, att: Int) = { rows(row).attributes(att) }

  def getCounts() = {    
    rows.groupBy(row => row.label.getOrElse(-1)).map(e => { (e._1, e._2.length) })    
  }

  def split(att: Int, th: Double) = {
    val part = partition(_.attributes(att) < th)
    (new Data(part._1.toSeq), new Data(part._2.toSeq))
  }

  def findRandomSplit() = {    
    var att = Random.nextInt(nb_attributes)
    var th = -1.0    
    var att_vector = Random.shuffle(getAttribute(att))    
    att_vector = att_vector.distinct    
    if (att_vector.length > 1) {      
      th = math.min(att_vector(0),att_vector(1)) + (math.abs(att_vector(0) - att_vector(1)) / 2.0)
    } else {      
      att = -1
    }    
    Split(att,th)    
  }
  
}

/*
//########################################
case class IncompatibleDataTypeException(message: String) extends Exception(message)

trait DataDNA {

  // TODO: move the commented code in test suites
  //val rowtest = new RowDNA[Double,Seq[Double], Int](Seq(1.5,2.5,3.5),Some(1))
  //println(rowtest)
  //println(rowtest.nbAttributes)
  //println(rowtest.isLabeled)
  //println(rowtest.attributes(0))

  // toy rows
  //val r1 = new RowDNA[Double,Seq[Double], Int](Seq(1.0,1.35,1.5,1.99),Some(0))
  //val r2 = new RowDNA[Double,Seq[Double], Int](Seq(2.0,2.35,2.5,2.99),Some(1))
  //val r3 = new RowDNA[Double,Seq[Double], Int](Seq(3.0,3.35,3.5,3.99),Some(2))
  
  //val datatest = new Data2(Seq(r1,r2,r3))
  //println(datatest.findRandomSplit)
  //println(datatest.split(0,1.5)._1)
  //println(datatest.split(0,1.5)._2)
  //println(datatest.getCounts)
  //println(datatest.getLabel(1))
  //println(datatest.getLabels(Seq(1,2)))
  //println(datatest.getObject(1))
  //println(datatest.getObjects(Seq(0,2)))
  //println(datatest.getAttribute(1))
  //println(datatest.getAttributes(Seq(1,2)))
  //println(datatest)
  //println(datatest.nb_objects)  
  //println(datatest.nb_attributes)
  //println(datatest.nb_classes)
  //println(datatest.labeled)
  //println(datatest.map(_.attributes(0)))
  //println(datatest.map(_.label))
  //println(datatest.partition(_.attributes(1) < 2.5))  

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
  def loadCSV(uri: String, label: Int, delimiter:String = " ")

  def split(attr: Int, thr: Double): (DataDNA, DataDNA)

  def getObject(index : Int) : DataDNA = { getObjects(Traversable(index)) }
  def getObjects(indexes : Traversable[Int]) : DataDNA

  def getAttribute(index : Int) : DataDNA = { getAttributes(Traversable(index)) }
  def getAttributes(indexes : Traversable[Int]) : DataDNA

  /*
  implicit def dataDNAToSeq(d: DataDNA) = {
    // TODO: implicit cast to a scala type?
    println("IMPLICIT CALL")    
  }
  */
  
  def findRandomSplit() : Split
  
  def getLabel(index : Int) : TY = { getLabels(Traversable(index)) }
  def getLabels(indexes : Traversable[Int]) : TY

  def getCounts(): Map[data_type,Int]

  def getValue(i: Int, j: Int) : data_type

  def describe
}

class Data extends DataDNA with Traversable[Seq[Double]] {
  type data_type = Double
  type TX = Seq[Seq[data_type]]
  type TY = Seq[data_type]
  var inputs: Seq[Seq[data_type]] = Seq.empty
  var labels: Seq[data_type] = Seq.empty

  // TODO: use labels as well
  // currently the foreach
  // does not take labels into account
  def foreach[U](f: Seq[Double] => U): Unit = {
    inputs.foreach(f)
  }

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

  def loadCSV(uri: String, label: Int, delimiter:String) = { 
    // TODO: take label into account
    // TODO: avoid the 2 passes over the CSV file
    labeled = true
    val p1 = Source.fromFile(uri).getLines() map {
      line => val fields = line.split(delimiter)
        fields(0).toDouble      
    }
    labels = p1.toList.toSeq
    nb_classes = labels.distinct.length

    val p2 = Source.fromFile(uri).getLines() map {
      line => val fields = line.split(delimiter)
        fields.drop(1).map(_.toDouble).toList.toSeq 
    } 
    inputs = p2.toList
    nb_objects = inputs.length
    nb_attributes = inputs(0).length    
  }

  def split(att: Int, th: Double): (Data, Data) = {
    val partOne = new Data
    val partTwo = new Data
    if (labeled) {      
      val zipped = inputs.zip(labels).partition(i => i._1(att) < th)      
      partOne.load(zipped._1.unzip._1, zipped._1.unzip._2)
      partTwo.load(zipped._2.unzip._1, zipped._2.unzip._2)
    } else {
      val splitted = inputs.partition(_(att) < th)
      partOne.load(splitted._1)
      partTwo.load(splitted._2)
    }
    (partOne, partTwo)
  }

  def findRandomSplit() : Split = {    
    val rand = new Random
    var att = Random.nextInt(nb_attributes)
    var th = -1.0
    var att_vector = rand.shuffle(inputs.map(_(att)))
    att_vector = att_vector.distinct    
    if (att_vector.length > 1) {      
      th = math.min(att_vector(0),att_vector(1)) + (math.abs(att_vector(0) - att_vector(1)) / 2.0)
    } else {      
      att = -1
    }    
    Split(att,th)
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
    println("Is there labels: " + labeled)
    println("How many labels: " + nb_classes)
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
//########################################
*/

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
  var labels = Seq.empty[Double]
  val inputs: Seq[RowDNA[Double,Seq[Double], Int]] = for (i:Int <- 0 until numInstances) yield {
    val a:Double = rand.nextInt(10)
    val b:Double = rand.nextInt(10)
    val c:Double = rand.nextInt(100)
    val d:Seq[Double] = Seq.fill(math.max(3,numFeatures) - 3)(rand.nextFloat())
    val y:Int = if ((a+b) > 0 && (a+b) < 6) { 0 } else if ((a+b) >= 6 && (a+b) < 12) { 1 } else { 2 }
    if (labeled) {
      new RowDNA[Double,Seq[Double], Int](Seq(a,b,c)++d,Some(y))
    } else {
      new RowDNA[Double,Seq[Double], Int](Seq(a,b,c)++d)
    }      
  }  
  new Data(inputs)
}
/*
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
}*/
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
