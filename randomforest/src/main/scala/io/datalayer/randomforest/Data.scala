package io.datalayer.randomforest


/*import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed.{IndexedRow, IndexedRowMatrix}
import org.apache.spark.rdd._*/

/*
  A few temporary classes to handle data...
*/
case class Label(label: Int)
case class Labeled(input: Seq[Float], label: Label)
case class Unlabeled(input: Seq[Float])



trait DataDNA {
  //implicit def toType(i: Any) : T = {i.asInstanceOf[T]}
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
  //def loadCSV

  //def partition(p: (A) => Boolean): ((A,T), (A,T))

  def getObject(index : Int) : TX = { getObjects(Traversable(index)) }
  def getObjects(indexes : Traversable[Int]) : TX

  def getAttribute(index : Int) : TX = { getAttributes(Traversable(index)) }
  def getAttributes(indexes : Traversable[Int]) : TX

  def getLabel(index : Int) : TY = { getLabels(Traversable(index)) }
  def getLabels(indexes : Traversable[Int]) : TY

  def getValue(i: Int, j: Int) : data_type

  def describe
}

class Data extends DataDNA {
  type data_type = Double
  type TX = Seq[Seq[data_type]]
  type TY = Seq[data_type]
  var inputs: TX = Seq.empty
  var labels: TY = Seq.empty

  def load(X: TX, Y: TY = Seq.empty) {
    // TODO: add some check somewhere
    // such as X.length == Y.length

    inputs = X
    nb_objects = X.length
    nb_attributes = X(0).length

    // Check if we have a labels
    if (Y.size != 0) {
      labeled = true
      labels = Y
      nb_classes = labels.distinct.length
    } else {
      labeled = false
      labels = Seq.empty
    }
  }

  //def loadCSV { println("Data loadCSV") }

  //def partition(p: (A) => Boolean): ((A,T), (A,T)) = {}

  def getObjects(indexes : Traversable[Int]) : TX = {
    indexes.map{i => inputs(i)}.toSeq
  }


  def getAttributes(indexes : Traversable[Int]) : TX = {
    //indexes.map(i => i.input(att))
    Seq(Seq(1.0))
  }

  def getLabels(indexes : Traversable[Int]) : TY = {
    indexes.map{i => labels(i)}.toSeq
  }
  def getValue(i: Int, j: Int) : data_type = { inputs(i)(j) }
  def describe { println("Data describe") }
}

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
