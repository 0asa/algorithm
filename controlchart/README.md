-------------------------------------------------------------------------------
```
  _______    ____      _       _
 _______    |    \ ___| |_ ___| |___ _ _ ___ ___ 
  ________  |  |  | .'|  _| .'| | .'| | | -_|  _|
 ________   |____/|__,|_| |__,|_|__,|_  |___|_|
                                    |___|        

 #datalayer-model-controlchart
```
-------------------------------------------------------------------------------
# Control Chart

[Control chart](http://en.wikipedia.org/wiki/Control_chart) or Shewhart Control Chart is a method to detect abnormal
events among a time series. Events must only be numbers.

 # Test
 If you want to check the improvements the spark library can introduce, you can find a test `ControlChartTest` in
 `src/test/scala/io/datalayer/controlchart` that tests 40,000,000 events.

 if you want to change the number of workers in you spark context, you have to set the following lines
 ```scala
    val conf = new SparkConf().setMaster("local[8]").setAppName("Simple Application")
 ```
where `local[8]` indicates the number of workers.

Then run the test alone to check the time spent (in millisecond).

-------------------------------------------------------------------------------
