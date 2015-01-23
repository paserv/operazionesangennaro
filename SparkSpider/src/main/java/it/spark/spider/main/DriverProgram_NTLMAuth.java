package it.spark.spider.main;

import it.queue.QueueLogger;
import it.queue.RunnableQueue;
import it.spark.spider.accumulator.ListAccumulatorParam;
import it.spark.spider.job.IntranetSpiderJob_NTLMAuth;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.spark.Accumulable;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

import scala.Some;

public class DriverProgram_NTLMAuth {

	private static Logger LOGGER = Logger.getLogger(DriverProgram_NTLMAuth.class.getName());
	
	protected static int MULTITHREADING_FACTOR = 10;
	protected static String USERNAME = "servill7";
	protected static String PASSWORD = "Paolos20";
	
	protected static int MAX_CYCLE = 0;
	
	private static String rootURL = "https://intranet.postepernoi.poste/";

	public static void main(String[] args) {
		try {
			FileHandler fileHandler = new FileHandler(DriverProgram_NTLMAuth.class.getName() + ".log", true);
			SimpleFormatter formatter = new SimpleFormatter();  
			fileHandler.setFormatter(formatter);
			LOGGER.addHandler(fileHandler);
			LOGGER.setLevel(Level.ALL);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		
		USERNAME = args[0];
		PASSWORD = args[1];
		MULTITHREADING_FACTOR = Integer.valueOf(args[3]);
		MAX_CYCLE = Integer.valueOf(args[4]);
		
		SparkConf conf;
		if (args[2].equalsIgnoreCase("local")) {
			conf = new SparkConf().setAppName("Spider").setMaster("local[" + MULTITHREADING_FACTOR + "]");
		} else {
			conf = new SparkConf().setAppName("Spider");
		}
		
		JavaSparkContext sc = new JavaSparkContext(conf);

		final Accumulable<List<String>, String> toVisit = new Accumulable<List<String>, String>(new ArrayList<String>(), new ListAccumulatorParam(), new Some<String>("To Visit"));
		toVisit.add(rootURL);

		final Accumulable<List<String>, String> alreadyVisited = new Accumulable<List<String>, String>(new ArrayList<String>(), new ListAccumulatorParam(), new Some<String>("Already Visited"));

		final Accumulable<List<String>, String> linksToPost = new Accumulable<List<String>, String>(new ArrayList<String>(), new ListAccumulatorParam(), new Some<String>("Links To Post"));

		long startTime = System.currentTimeMillis();
		
		List<String> listForMap;
		
		int currCycle = 0;
		
		while (true) {
			listForMap = new ArrayList<String>();
			List<String> currentToVisit = toVisit.value();
			List<String> currentVisited = alreadyVisited.value();
			LOGGER.info("TOTAL TO VISIT " + toVisit.value().size());
			LOGGER.info("TOTAL ALREADYVISITED " + alreadyVisited.value().size());
			System.out.println("TOTAL TO VISIT " + toVisit.value().size());
			System.out.println("TOTAL ALREADYVISITED " + alreadyVisited.value().size());

			for (String currAllToVisit : currentToVisit) {
				if (!currentVisited.contains(currAllToVisit)) {
					listForMap.add(currAllToVisit);
				}
			}
			System.out.println("REAL TO VISIT " + listForMap.size());
			LOGGER.info("REAL TO VISIT " + listForMap.size());

			if (listForMap.size() == 0 || currCycle > MAX_CYCLE) break;
			
			JavaRDD<String> realToVisit = sc.parallelize(listForMap);
			JavaRDD<Integer> fakeMap = realToVisit.map(new Function<String, Integer>() {

				@Override
				public Integer call(String currUrl) throws Exception {
					
					alreadyVisited.add(currUrl);					
					
					List<String> newLinks = new ArrayList<String>();
					List<String> foundPostLinks = new ArrayList<String>();
					
					IntranetSpiderJob_NTLMAuth currTask = new IntranetSpiderJob_NTLMAuth(currUrl, USERNAME, PASSWORD, newLinks, foundPostLinks);
					currTask.executeJob();
					
					for (String currNewUrl : newLinks) {
						toVisit.add(currNewUrl);
					}
					for (String currPostToVisit : foundPostLinks) {
						linksToPost.add(currPostToVisit);
					}
						
					LOGGER.info("FOUND " + newLinks.size() + " NEW LINKS");
					System.out.println("FOUND " + newLinks.size() + " NEW LINKS");
					
					return 1;
					
				}
			});
			
//			int fakeReduce = fakeMap.reduce((a, b) -> a + b);
			int fakeReduce = fakeMap.reduce(new Function2<Integer, Integer, Integer>() {
				
				@Override
				public Integer call(Integer v1, Integer v2) throws Exception {
					return v1 + v2;
				}
			});
			LOGGER.info("Link Visited: " + fakeReduce);
			LOGGER.info("END CYCLE");
			System.out.println("Link Visited: " + fakeReduce);
			System.out.println("END CYCLE");
		
			
		}

		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		
		System.out.println("SIZE ALREADY VISITED TOTAL LIST WITH DUPLICATE " + alreadyVisited.value().size());
		LOGGER.info("SIZE ALREADY VISITED TOTAL LIST WITH DUPLICATE " + alreadyVisited.value().size());
		
		System.out.println("REAL VISITED NO DUPLICATE");
		List<String> noDuplicateVisited = new ArrayList<>(new LinkedHashSet<String>(alreadyVisited.value()));
		for (String currUrl : noDuplicateVisited) {
			System.out.println(currUrl);
		}
		print(noDuplicateVisited, "exploredLinks.txt", elapsedTime);
		
		System.out.println("POST TO PARSE NO DUPLICATE");
		List<String> noDuplicatePostLinks =	new ArrayList<>(new LinkedHashSet<String>(linksToPost.value()));
		for (String currUrl : noDuplicatePostLinks) {
			System.out.println(currUrl);
		}
		print(noDuplicatePostLinks, "postLinks.txt", elapsedTime);
		
	}
	
	public static void print(List<String> list, String fileName, long time) {
		try {
			FileWriter fstream = new FileWriter(fileName);
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
