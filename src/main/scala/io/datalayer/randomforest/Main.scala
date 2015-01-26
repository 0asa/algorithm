package io.datalayer.randomforest

object Main extends App {

    // Generating some data
    val train = dataGenerator.genLabeled(numInstances=200, numFeatures=10)
    val test = dataGenerator.genLabeled(numInstances=200, numFeatures=10)

    // Preparing the Extra-Trees forest
    val forest = new Forest(min_samples_split=10,n_estimators=100, max_features=5)
    println(forest)

    // Training the model
    forest.fit(train)
    println("Accuracy = " + forest.predictEval(test)._2)
}
