package io.datalayer.randomforest

/*
  A few temporary classes to handle data...
*/
case class Label(label: Int)
case class Labeled(input: Seq[Float], label: Label)
case class Unlabeled(input: Seq[Float])

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
    val d = Seq.fill(numFeatures - 3)(rand.nextFloat())

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
    val d = Seq.fill(numFeatures - 3)(rand.nextFloat())
    Unlabeled(Seq(a,b,c)++d)
  }
  x
}
}
