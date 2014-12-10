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

import org.apache.spark.sql._

//import org.apache.spark.sql.SchemaRDD
import org.apache.spark.ml.param._
import org.apache.spark.ml._
import org.apache.spark.sql.catalyst.analysis.Star

object Stat {
  def computeMean(input: Array[Double]): Double = {
    val mean = input.reduceLeft(_ + _) / input.length
    return mean
  }

  /** Compute empirical variance */
  def computeVariance(input: Array[Double], mean: Double): Double = {
    val variance = input.map(x => Math.pow(x - mean, 2)).reduceLeft(_ + _) / (input.length - 1)
    return variance
  }

  /** Compute naively the area under the curve. */
  def computeIntegral(xAxis: Array[Double], yAxis: Array[Double]): Double = {
    val step = Math.abs(xAxis(1) - xAxis(0))
    val integ = yAxis.map(x => x * step).reduceLeft(_ + _)
    return integ
  }

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


  var mean = 0.0
  var stdDev = 1.0

  var localDev = Array[Double]()

  var outliers = Array[Int]()

  var data = Array[Double]()

  def computeLimit(learnData: Array[Double]) = {
    data = learnData
    mean = Stat.computeMean(data)
    stdDev = Math.sqrt(Stat.computeVariance(data, mean))

    localDev = data.map(x => Stat.computeStdDev(x, mean))

    outliers = localDev.map(x => Math.abs(x)).zipWithIndex.filter(_._1 > 2*stdDev).map(_._2)
  }

  def summary() = {
    println("Mean = " + mean)
    println("Standard Deviation = " + stdDev)
    println("Sample size = " + data.length)
    print("\n")
    println("Outliers:")

    outliers.foreach(x => print("Data value : \t"  + data(x) + " \t-- " + x + "\n"))
  }

  val series = new MemXYSeries((1 to data.length).toList.map(_.toDouble).toSeq, data.map(_.toDouble).toSeq)
  series.plotStyle = XYPlotStyle.Points

  def plotASCII() = {
    output(ASCII, plot(series))
  }
  
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
    val data = dataset.collect.map(_.toVector).map(_(0).asInstanceOf[Double])
    cc.computeLimit(data)
    cc.summary()

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

