/**
 * **************************************************************
 * Licensed to the AOS Community (AOS) under one or more        *
 * contributor license agreements.  See the NOTICE file         *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The AOS licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 * **************************************************************
 */
package io.datalayer.controlchart

import java.io.File

import org.sameersingh.scalaplot.MemXYSeries
import org.sameersingh.scalaplot.XYPlotStyle
import org.sameersingh.scalaplot.Implicits._

import org.apache.spark.rdd.RDD
import org.apache.spark.sql._

//import org.apache.spark.sql.SchemaRDD
import org.apache.spark.ml.param._
import org.apache.spark.ml._
import org.apache.spark.sql.catalyst.analysis.Star

object Stat {
  def computeMean(input: RDD[Double]): Double = {
    val mean = input.reduce(_ + _) / input.count
    return mean
  }

  /** Compute empirical variance */
  def computeVariance(input: RDD[Double], mean: Double): Double = {
    val variance = input.map(x => Math.pow(x - mean, 2)).reduce(_ + _) / (input.count - 1)
    return variance
  }

  /** Compute naively the area under the curve. */
  /*def computeIntegral(xAxis: RDD[Double], yAxis: RDD[Double]): Double = {
    val step = Math.abs(xAxis(1) - xAxis(0))
    val integ = yAxis.map(x => x * step).reduce(_ + _)
    return integ
  }*/

  /** Compute the deviation from the mean. */
  def computeStdDev(value: Double, mean: Double): Double = {
    return Math.abs(value - mean)
  }
}

/** File reader utility
  * @param filePath the absolute/relative path of the csv.
  * */
class ReadCSV(filePath: String) {

  def getColumn(col: Int): Array[Double] = {
    val column = event.map(x => x(col))
    return column.toArray
  }

  def getLine(col: Int): Array[Double] = {
    val line = event(col)
    return line

  }

  val file = scala.io.Source.fromFile(filePath)
  val parserIt = file.getLines().drop(0).map(_.split(","))

  val colNames = parserIt.next()

  val event = scala.collection.mutable.ArrayBuffer.empty[Array[Double]]
  parserIt.foreach(a => event.append(a.map(_.toDouble)))

  file.close()
}

class ReadCSVFolder(folderPath: String) {
  def listFiles(f: File): Array[File] = {
    return f.listFiles.sorted
  }

  val folder = new File(folderPath)
  val files = listFiles(folder).filter(_.toString.endsWith(".csv"))
  val data = files.map(x => new ReadCSV(x.toString))
}

/**
 *  The Control Chart class perform a basic control chart. It simply oulines
 * data which are 2 times greater than the standard deviation.
 *
 * @param data An array of float representing the data to analyze.
 * */
class ControlChart {

  var computed = false

  var mean = 0.0
  var stdDev = 1.0

  var outliers = Array[Long]()

  def computeLimit(data: RDD[Double]) = {
    computed = true
    val mu = Stat.computeMean(data)
    val dev = Math.sqrt(Stat.computeVariance(data, mu))


    outliers = data.map((x:Double) => Math.abs(x - mu)).zipWithIndex.filter(_._1 > 2*dev).map(_._2).collect

    mean = mu
    stdDev = dev
  }

  def summary(data: RDD[Double]) = {
    if (!computed) computeLimit(data)

    println("Mean = " + mean)
    println("Standard Deviation = " + stdDev)
    println("Sample size = " + data.count)
    print("\n")
    println("Outliers (" + outliers.length + ") :")

    outliers.foreach(x => print("Data index : \t" + x + "\n"))
  }

  /*val series = new MemXYSeries((1 to data.length).toList.map(_.toDouble).toSeq, data.map(_.toDouble).toSeq)
  series.plotStyle = XYPlotStyle.Points

  def plotASCII() = {
    output(ASCII, plot(series))
  }*/

}


class ControlChartPipe extends Estimator[ControlChartModel] {

  override def fit(dataset: SchemaRDD, paramMap: ParamMap): ControlChartModel = {
    val ccm = new ControlChartModel(this, paramMap)
    ccm.transform(dataset, paramMap)
    return ccm
  }

  override def transformSchema(schema: StructType, paramMap: ParamMap): StructType = {
    return schema
  }
}

class ControlChartModel(override val parent: ControlChartPipe,
                        override val fittingParamMap: ParamMap)
  extends Model[ControlChartModel]  {

  var cc = new ControlChart

  override def transform(dataset: SchemaRDD, paramMap: ParamMap): SchemaRDD = {
    val data = dataset.map(_(0).asInstanceOf[Double])
    cc.computeLimit(data)
    cc.summary(data)

    val predict: Int => Int = (index) => {
      if (cc.outliers.filter(_ == index) == None) {
        0
      } else {
        1
      }
    }

    //dataset.select(Star(None), predict.call())
    return dataset
  }

  override def transformSchema(schema: StructType, paramMap: ParamMap): StructType = {
    schema
  }
}

