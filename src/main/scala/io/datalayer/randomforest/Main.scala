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
