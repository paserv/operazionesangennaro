package it.spark.spider.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

public class DummyTest {

	protected static final int MULTITHREADING_FACTOR = 25;

	public static void main(String[] args) {
//		SparkConf conf = new SparkConf().setAppName("Spider").setMaster("local[" + MULTITHREADING_FACTOR + "]");
		SparkConf conf = new SparkConf().setAppName("Test");
		JavaSparkContext sc = new JavaSparkContext(conf);

		List<String> dummyList = new ArrayList<String>();
		for (int i = 0; i < 1000; i++) {
			dummyList.add("string_" + i);
		}
		
		long startTime = System.currentTimeMillis();
		
		JavaRDD<String> parallelList = sc.parallelize(dummyList);
		
		JavaRDD<Integer> fakeMap = parallelList.map(new Function<String, Integer>() {

			@Override
			public Integer call(String v1) throws Exception {
				System.out.println("STARTING TASK " + v1);
				Thread.sleep(10000);
				System.out.println("END TASK " + v1);
				return 1;
			}
			
		});
		
		int fakeReduce = fakeMap.reduce(new Function2<Integer, Integer, Integer>() {
			
			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1 + v2;
			}
		});
		
		
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		
		try {
			FileWriter fstream = new FileWriter("test.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("ELAPSED TIME: " + elapsedTime);
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
}
