[![Datalayer](http://datalayer.io/ext/images/logo_horizontal_072ppi.png)](http://datalayer.io)

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

