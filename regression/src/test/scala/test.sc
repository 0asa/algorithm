object hello {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  var xs = List(1, 2, 3)                          //> xs  : List[Int] = List(1, 2, 3)
  var x = 0                                       //> x  : Int = 0
  xs foreach println                              //> 1
                                                  //| 2
                                                  //| 3
  while (x < 3) {
    println(x)
    x += 1
    Thread.sleep(200)
  }                                               //> 0
                                                  //| 1
                                                  //| 2

}