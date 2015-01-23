package it.multithread.spider;

import it.multithread.spider.bean.LinksMap;
import it.multithread.spider.job.SimpleSpiderJobMT_NTLMAuth;
import it.queue.Queue;
import it.queue.QueueLogger;
import it.queue.TimeoutException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

public class MainMT {
	
	protected static final String USERNAME = "servill7";
	protected static final String PASSWORD = "Paolos20";
	
	protected static final int MULTITHREADING_FACTOR = 25;

	public static void main(String[] args) {
		long start;
		long end;
		QueueLogger.setup(Level.ALL);
		
		LinksMap links = new LinksMap();
		
		Queue queue = new Queue(Integer.valueOf(args[2]), 100000L, 0L);
		queue.setRollback(false);
		SimpleSpiderJobMT_NTLMAuth spid = new SimpleSpiderJobMT_NTLMAuth("https://intranet.postepernoi.poste/", args[0], args[1], links);
		spid.setName("root");
		queue.addThread(spid);

		start = System.currentTimeMillis();
		try {
			queue.executeAndWait();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		end = System.currentTimeMillis();
		long elapsedTime = end - start;
		System.out.println(((end - start)/1000) + " seconds elapsed");
		
		links.printExploredLinks(elapsedTime);
		links.printPostLinks(elapsedTime);
		
//		PostMap map = new PostMap();
//		Queue postQueue = new Queue(100, 10000L, 0L);
//		postQueue.setRollback(false);
//		
////		Collection<String> linksToExplore = links.getPostLinks().values();
//		Collection<String> linksToExplore = readPostLinks();
////		Collection<String> linksToExplore = new ArrayList<String>();
////		linksToExplore.add("https://intranet.postepernoi.poste/passato-e-futuro-si-incontrano-a-bari/");
//		for (String currLink : linksToExplore) {
//			PostTask pt = new PostTask(currLink, map);
//			pt.setName(currLink);
//			postQueue.addThread(pt);
//		}
//		start = System.currentTimeMillis();
//		try {
//			postQueue.executeAndWait();
//		} catch (TimeoutException e) {
//			e.printStackTrace();
//		}
//		end = System.currentTimeMillis();
//		
//		System.out.println(((end - start)/1000)/60 + " minutes elapsed");
//		
//		map.printPosts();
//		map.printComments();
		
	}

	private static Collection<String> readPostLinks() {
		ArrayList<String> result = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("postLinks.txt"));
	        String line = br.readLine();
	        while (line != null) {
	            result.add(line);
	            line = br.readLine();
	        }

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
        }
		return result;
	}
	
	
}
