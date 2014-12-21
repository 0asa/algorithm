-------------------------------------------------------------------------------
```
             ___       __       __                 
   _______  / _ \___ _/ /____ _/ /__ ___ _____ ____
  _______  / // / _ `/ __/ _ `/ / _ `/ // / -_) __/
 _______  /____/\_,_/\__/\_,_/_/\_,_/\_, /\__/_/   
                                    /___/                 

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
    val sc = SparkContextManager.getSparkContext(8)
 ```
where `8` indicates the number of workers.

```
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

-------------------------------------------------------------------------------
