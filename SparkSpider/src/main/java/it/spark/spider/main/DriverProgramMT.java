package it.spark.spider.main;

import it.queue.Queue;
import it.queue.QueueLogger;
import it.spark.spider.accumulator.ListAccumulatorParam;
import it.spark.spider.job.IntranetSpiderJobMT;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Level;

import org.apache.spark.Accumulable;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;

import scala.Some;

public class DriverProgramMT {

	protected static final int MULTITHREADING_FACTOR = 25;
	private static String rootURL = "https://intranet.postepernoi.poste/";

	public static void main(String[] args) {
//		SparkConf conf = new SparkConf().setAppName("Spider").setMaster("local[" + MULTITHREADING_FACTOR + "]");
		SparkConf conf = new SparkConf().setAppName("Spider");
		JavaSparkContext sc = new JavaSparkContext(conf);

		final Accumulable<List<String>, String> toVisit = new Accumulable<List<String>, String>(new ArrayList<String>(), new ListAccumulatorParam(), new Some<String>("To Visit"));
		toVisit.add(rootURL);

		final Accumulable<List<String>, String> alreadyVisited = new Accumulable<List<String>, String>(new ArrayList<String>(), new ListAccumulatorParam(), new Some<String>("Already Visited"));

		final Accumulable<List<String>, String> linksToPost = new Accumulable<List<String>, String>(new ArrayList<String>(), new ListAccumulatorParam(), new Some<String>("Links To Post"));

		long startTime = System.currentTimeMillis();
		
		List<String> listForMap;
		
		do {
			listForMap = new ArrayList<String>();
			List<String> currentToVisit = toVisit.value();
			List<String> currentVisited = alreadyVisited.value();
			System.out.println("TOTAL TO VISIT " + toVisit.value().size());
			System.out.println("TOTAL ALREADYVISITED " + alreadyVisited.value().size());

			for (String currAllToVisit : currentToVisit) {
				if (!currentVisited.contains(currAllToVisit)) {
					listForMap.add(currAllToVisit);
				}
			}
			System.out.println("REAL TO VISIT " + listForMap.size());

			JavaRDD<String> realToVisit = sc.parallelize(listForMap);
			JavaRDD<Integer> fakeMap = realToVisit.mapPartitions(new FlatMapFunction<Iterator<String>, Integer>() {

				@Override
				public Iterable<Integer> call(Iterator<String> t) throws Exception {
					
					ArrayList<Integer> result = new ArrayList<>();
					List<String> newLinks = Collections.synchronizedList(new ArrayList<String>());
					List<String> foundPostLinks = Collections.synchronizedList(new ArrayList<String>());
					QueueLogger.setup(Level.FINE);
					Queue localQueue = new Queue(MULTITHREADING_FACTOR, 2000L, 0L);
					localQueue.setRollback(false);
					while (t.hasNext()) {
						String currUrl = t.next();
						alreadyVisited.add(currUrl);
						IntranetSpiderJobMT currTask = new IntranetSpiderJobMT(currUrl, newLinks, foundPostLinks);
						currTask.setName(currUrl);
						localQueue.addThread(currTask);
						result.add(1);	
						}
					
					localQueue.executeAndWait();
					
					for (String currNewUrl : newLinks) {
						toVisit.add(currNewUrl);
					}
					for (String currPostToVisit : foundPostLinks) {
						linksToPost.add(currPostToVisit);
					}
						
					System.out.println("FOUND " + newLinks.size() + " NEW LINKS");
					
					return result;
					
				}
			});
			
//			int fakeReduce = fakeMap.reduce((a, b) -> a + b);
			int fakeReduce = fakeMap.reduce(new Function2<Integer, Integer, Integer>() {
				
				@Override
				public Integer call(Integer v1, Integer v2) throws Exception {
					return v1 + v2;
				}
			});
			System.out.println("Link Visited: " + fakeReduce);
			System.out.println("END CYCLE");
		
			
		} while (listForMap.size() > 0);

		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		
		System.out.println("SIZE ALREADY VISITED TOTAL LIST WITH DUPLICATE " + alreadyVisited.value().size());
		
		System.out.println("REAL VISITED NO DUPLICATE");
		List<String> noDuplicateVisited = new ArrayList<>(new LinkedHashSet<String>(alreadyVisited.value()));
		for (String currUrl : noDuplicateVisited) {
			System.out.println(currUrl);
		}
		
		System.out.println("POST TO PARSE NO DUPLICATE");
		List<String> noDuplicatePostLinks =	new ArrayList<>(new LinkedHashSet<String>(alreadyVisited.value()));
		for (String currUrl : noDuplicatePostLinks) {
			System.out.println(currUrl);
		}
		print(noDuplicatePostLinks, elapsedTime);
		
	}
	
	public static void print(List<String> list, long time) {
		try {
			FileWriter fstream = new FileWriter("post_links.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("SIZE: " + list.size() + " ELAPSED TIME: " + time);
			out.newLine();
			for (String currLink : list) {
				out.write(currLink);
				out.newLine();
			}
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
