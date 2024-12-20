import org.apache.spark.sql.SparkSession

object exploreDagAndSparkUI_4 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("Dag and Spark UI")
      .getOrCreate()

    val sprk_ctx = spark.sparkContext

    try {
      val rdd = sprk_ctx.parallelize(1 to 10000, 10)

      /* Transformation Steps*/
      // Filter even numbers
      val evenNumbersRDD = rdd.filter(x => x % 2 == 0)

      // Multiply each number by 10
      val multipliedRDD = evenNumbersRDD.map(x => x * 10)

      // Generate tuples (x, x + 1)
      val flatMappedRDD = multipliedRDD.flatMap(x => Seq((x, x + 1)))


      // Reduce by key, summing the keys
      val reducedRDD = flatMappedRDD.reduceByKey((a, b) => a + b)

      //Action
      val result = reducedRDD.collect()

      result.take(10).foreach(println)

    } finally {
      Thread.currentThread().join()
      spark.stop()
    }
  }
}
