[![Datalayer](http://datalayer.io/ext/images/logo_horizontal_072ppi.png)](http://datalayer.io)

# Introduction

Datalayer Model is a module that can be deployed and configured in the
[Datalayer Big Data Science Platform](http://datalayer.io/platform).

[Read more](src/site/markdown/index.md) on this component.

# Trees

Implementation of a (not yet) distributed ensemble of decision trees algorithm.

**Important:** This is a work in progress. The current implementation corresponds to the [Extra-Trees](http://www.montefiore.ulg.ac.be/~ernst/uploads/news/id63/extremely-randomized-trees.pdf) (Extremely randomized trees) method.

Here is a small sample code:

```scala
package io.datalayer.randomforest

object Main extends App {

    /* ----------------------------------------
     * Using the old data representation
     * ---------------------------------------- */

    // Generating some data
    val train = dataGenerator.genLabeled(numInstances=200, numFeatures=10)
    val test = dataGenerator.genLabeled(numInstances=200, numFeatures=10)

    // Preparing the Extra-Trees forest
    val forest = new Forest(min_samples_split=10,n_estimators=100, max_features=5)
    println(forest)

    // Training the model
    forest.fit(train)
    println("Accuracy = " + forest.predictEval(test)._2)

    /* ----------------------------------------
     * Using the Datalayer DataDNA
     * ---------------------------------------- */

    // Generating some data
    val labeled = dataGenerator.genData(50,10,true)

    // Preparing the Extra-Trees forest
    val trees = new Forest(min_samples_split=10,n_estimators=10,max_features=5)

    // Training the model
    trees.fit(labeled)

    // Print the (re-substitution) accuracy
    val preds = trees.predict(labeled)
    println("Score: " + trees.score(preds, labeled.getLabels()))
}
```

# Control Chart

[Control chart](http://en.wikipedia.org/wiki/Control_chart) or Shewhart Control Chart is a method to detect abnormal
events among a time series. Events must only be numbers.

 # Test
 If you want to check the improvements the spark library can introduce, you can find a test `ControlChartTest` in
 `src/test/scala/io/datalayer/controlchart` that tests 40,000,000 events.

 if you want to change the number of workers in you spark context, you have to set the following lines

```scala
val sc = SparkContextManager.getSparkContext(8)
```

where `8` indicates the number of workers.

```scala
val measures = sc.parallelize(Array[Double](5, 5, 5, 5, 5, 5, 5, 5, 19))
val cc = new ControlChart()
cc.setStdLimit(5.0)
cc.computeLimit(measures)
cc summary(measures)
```

# License

Copyright 2014 Datalayer http://datalayer.io

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
