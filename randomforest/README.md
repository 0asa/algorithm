![](http://datalayer.io/ext/images/logo_horizontal_072ppi.png)

## datalayer-model-trees

Implementation of a (not yet) distributed ensemble of decision trees algorithm.

**Important:** This is a work in progress. The current implementation corresponds to the [Extra-Trees](http://www.montefiore.ulg.ac.be/~ernst/uploads/news/id63/extremely-randomized-trees.pdf) (Extremely randomized trees) method.

Here is a small sample code:

```
package io.datalayer.randomforest

object Main extends App {

    // Generating some data
    val train = dataGenerator.genLabeled(numInstances=200, numFeatures=10)
    val test = dataGenerator.genLabeled(numInstances=200, numFeatures=10)

    // Preparing the Extra-Trees forest
    val forest = new Forest(min_samples_split=10,n_estimators=100, max_features=5)
    println(Forest.printParams(forest))

    // Training the model
    forest.fit(train)
    println("Accuracy = " + forest.predictEval(test)._2)
}
```
