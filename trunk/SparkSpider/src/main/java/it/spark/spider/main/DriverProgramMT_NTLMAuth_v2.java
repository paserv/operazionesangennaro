package it.spark.spider.main;

import it.queue.Queue;
import it.queue.QueueLogger;
import it.spark.spider.accumulator.HashSetAccumulatorParam;
import it.spark.spider.job.IntranetSpiderJobMT_NTLMAuth;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.spark.Accumulable;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;

import scala.Some;

public class DriverProgramMT_NTLMAuth_v2 {

	private static Logger LOGGER = Logger.getLogger(DriverProgramMT_NTLMAuth_v2.class.getName());

	protected static int MULTITHREADING_FACTOR = 10;
	protected static String USERNAME = "servill7";
	protected static String PASSWORD = "Paolos20";
	private static String rootURL = "https://intranet.postepernoi.poste/";

	public static void main(final String[] args) {

		try {
			FileHandler fileHandler = new FileHandler(DriverProgramMT_NTLMAuth_v2.class.getName() + ".log", true);
			SimpleFormatter formatter = new SimpleFormatter();  
			fileHandler.setFormatter(formatter);
			LOGGER.addHandler(fileHandler);
			LOGGER.setLevel(Level.ALL);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}

		USERNAME = args[0];
		PASSWORD = args[1];

		SparkConf conf;
		if (args[2].equalsIgnoreCase("local")) {
			conf = new SparkConf().setAppName("Spider").setMaster("local[" + MULTITHREADING_FACTOR + "]");
		} else {
			conf = new SparkConf().setAppName("Spider");
		}

		JavaSparkContext sc = new JavaSparkContext(conf);

		final Accumulable<Set<String>, String> toVisit = new Accumulable<Set<String>, String>(new HashSet<String>(), new HashSetAccumulatorParam(), new Some<String>("To Visit"));
		toVisit.add(rootURL);

		final Accumulable<Set<String>, String> alreadyVisited = new Accumulable<Set<String>, String>(new HashSet<String>(), new HashSetAccumulatorParam(), new Some<String>("Already Visited"));

		final Accumulable<Set<String>, String> linksToPost = new Accumulable<Set<String>, String>(new HashSet<String>(), new HashSetAccumulatorParam(), new Some<String>("Links To Post"));

		long startTime = System.currentTimeMillis();

		List<String> listForMap;

		while (true) {
			listForMap = new ArrayList<String>();
			Set<String>  currentToVisit = toVisit.value();
			Set<String>  currentVisited = alreadyVisited.value();
			LOGGER.info("TOTAL TO VISIT " + toVisit.value().size());
			LOGGER.info("TOTAL ALREADYVISITED " + alreadyVisited.value().size());
			System.out.println("TOTAL TO VISIT " + toVisit.value().size());
			System.out.println("TOTAL ALREADYVISITED " + alreadyVisited.value().size());

			for (String currAllToVisit : currentToVisit) {
				if (!currentVisited.contains(currAllToVisit)) {
					listForMap.add(currAllToVisit);
				}
			}
			
			if (listForMap.size() == 0) break;
			
			LOGGER.info("REAL TO VISIT " + listForMap.size());
			System.out.println("REAL TO VISIT " + listForMap.size());

			JavaRDD<String> realToVisit = sc.parallelize(listForMap);
			JavaRDD<Integer> fakeMap = realToVisit.mapPartitions(new FlatMapFunction<Iterator<String>, Integer>() {

				@Override
				public Iterable<Integer> call(Iterator<String> t) throws Exception {

					MULTITHREADING_FACTOR = Integer.valueOf(args[3]);
					ArrayList<Integer> result = new ArrayList<>();
					List<String> newLinks = Collections.synchronizedList(new ArrayList<String>());
					List<String> foundPostLinks = Collections.synchronizedList(new ArrayList<String>());
					QueueLogger.setup(Level.FINE);
					LOGGER.info("MULTITHREADING_FACTOR: " + MULTITHREADING_FACTOR);
					System.out.println("MULTITHREADING_FACTOR: " + MULTITHREADING_FACTOR);
					Queue localQueue = new Queue(MULTITHREADING_FACTOR, 2000L, 0L);
					localQueue.setRollback(false);
					while (t.hasNext()) {
						String currUrl = t.next();
						alreadyVisited.add(currUrl);
						IntranetSpiderJobMT_NTLMAuth currTask = new IntranetSpiderJobMT_NTLMAuth(currUrl, USERNAME, PASSWORD, newLinks, foundPostLinks);
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
					LOGGER.info("FOUND " + newLinks.size() + " NEW LINKS");
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
			
			LOGGER.info("Link Visited: " + fakeReduce);
			LOGGER.info("END CYCLE");
			System.out.println("Link Visited: " + fakeReduce);
			System.out.println("END CYCLE");


		}

		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;

		System.out.println("REAL VISITED " + alreadyVisited.value().size());
		LOGGER.info("REAL VISITED " + alreadyVisited.value().size());
		print(alreadyVisited.value(), "exploredLinks.txt", elapsedTime);
		
		System.out.println("POST TO PARSE " + linksToPost.value().size());
		print(linksToPost.value(), "postLinks.txt", elapsedTime);

	}

	public static void print(Set<String> list, String fileName, long time) {
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
