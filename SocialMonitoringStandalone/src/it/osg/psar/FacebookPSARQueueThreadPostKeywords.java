package it.osg.psar;

import it.osg.data.PSAR;
import it.osg.runnable.RunnableQueueImpl;
import it.osg.utils.FacebookUtils;
import it.queue.Queue;
import it.queue.TimeoutException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import com.csvreader.CsvWriter;


public class FacebookPSARQueueThreadPostKeywords {

	public static String resourcesFolder = "resources/";
	public static String outputFolder = "output/";
	public static char inputCharDelimiter = ';';

	public static String inputFile = "quotidiani.csv";
	public static String from = "13-05-2014 00:00:00";
	public static String to = "13-05-2014 23:59:59";

	public static int QUEUELENGHT = 32; //Numero massimo thread in stato running
	public static long QUEUE_CHECK_SLEEP = 5000L; //tempo dopo il quale viene eseguito il check per capire: 1)se il timeout è stato superato; 2)se può far partire nuovi thread prelevandoli dalla coda
	public static long QUEUE_TIMEOUT = 1000000000L; //timeout della coda

	public static String keywordString = "renzi";
	public static String keywordOperator = "OR";

	public static void main(String[] args) {

		//		System.getProperties().put("http.proxyHost", "proxy.gss.rete.poste");
		//		System.getProperties().put("http.proxyPort", "8080");
		//		System.getProperties().put("http.proxyUser", "rete\\servill7");
		//		System.getProperties().put("http.proxyPassword", "Paolos10");

		if (args != null && args.length > 0) {
			inputFile = args[0];
			from = args[1];
			to = args[2];
			QUEUELENGHT = Integer.valueOf(args[3]);
			QUEUE_CHECK_SLEEP = Long.valueOf(args[4]);
			QUEUE_TIMEOUT = Long.valueOf(args[5]);
			keywordString = args[6];
			keywordOperator = args[7];
		}

		//GET DATE
		String fromDay = from.substring(0, 10) + " 00:00:00";
		String toDay = to.substring(0, 10) + " 23:59:59";

		Queue queue = new Queue(QUEUELENGHT, QUEUE_CHECK_SLEEP, QUEUE_TIMEOUT);
		queue.setRollback(false);
		
		Hashtable<String, PSAR> result = new Hashtable<String, PSAR>();

		CsvWriter outWriter = openOutputFile(outputFolder + "FB_" + from.substring(0, 10) + "_TO_" + to.substring(0,10) + "_KEY_" + keywordString + "_" + keywordOperator + "_" + System.currentTimeMillis() + ".csv");

		String[] keywords = keywordString.split(",");
		if (keywordOperator.equalsIgnoreCase("AND")) {
			keywords = new String[1];
			keywords[0] = keywordString;
		}

		ArrayList<Hashtable<String, Set<String>>> postsGroupedByAuthor = new ArrayList<Hashtable<String,Set<String>>>();
		for (int i = 0; i < keywords.length; i++) {
			String currKeyword = keywords[i];
			Hashtable<String, Set<String>> currKeywordPosts = FacebookUtils.getPostByKeyword(currKeyword, fromDay, toDay);
			postsGroupedByAuthor.add(currKeywordPosts);
		}

		Hashtable<String, Set<String>> authorsWithPost = new Hashtable<String, Set<String>>();
		Iterator<Hashtable<String, Set<String>>> iter = postsGroupedByAuthor.iterator();
		while (iter.hasNext()) {
			Hashtable<String, Set<String>> current = iter.next();
			Enumeration<String> keys = current.keys();
			while (keys.hasMoreElements()) {
				String currKey = keys.nextElement();
				if (authorsWithPost.containsKey(currKey)) {	
					Set<String> alreadyExistentPostSet = authorsWithPost.get(currKey);
					alreadyExistentPostSet.addAll(current.get(currKey));					
				} else {
					authorsWithPost.putAll(current);
				}

			}

		}


		Enumeration<String> authors = authorsWithPost.keys();
		while (authors.hasMoreElements()) {
			String currAuthorID = authors.nextElement();
			PSAR idPSAR = getIdPSAR(result, currAuthorID, currAuthorID);

			Set<String> posts = authorsWithPost.get(currAuthorID);

			//SPLITTO 1 POST ALLA VOLTA ED INSERISCO UNA QUEUE DI THREAD
			Iterator<String> iterator = posts.iterator();
			while (iterator.hasNext()) {
				String currPostID = iterator.next();
				RunnableQueueImpl worker = new RunnableQueueImpl(currAuthorID, currPostID, idPSAR);
				worker.setName(currPostID);
				queue.addThread(worker);
			}
		}

		long start = System.currentTimeMillis();
		try {
			queue.executeAndWait();
		} catch (TimeoutException e1) {
			e1.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("Elapsed Time: " + (end - start)/1000 + " seconds");


		Enumeration<PSAR> elements = result.elements();
		while (elements.hasMoreElements()) {
			PSAR curr = elements.nextElement();

			Hashtable<String, Object> baseInfo = FacebookUtils.getBaseInfoFromJson(curr.getId());
			if (baseInfo.get("likes") != null && !((String) baseInfo.get("likes")).equals("")) {
				try {
					if (baseInfo.get("name") != null && !((String) baseInfo.get("name")).equals("")) {
						outWriter.write((String) baseInfo.get("name"));
					} else {
						outWriter.write(curr.getNome());
					}
					outWriter.write(curr.getId());
					outWriter.write(curr.getPostFromPage().toString());
					outWriter.write(curr.getPostFromFan().toString());
					outWriter.write(curr.getComments().toString());
					outWriter.write(curr.getLikes().toString());
					outWriter.write(curr.getShares().toString());
					outWriter.write(curr.getCommnetsFromPageToPostFromPage().toString());				
					outWriter.write(curr.getCommnetsFromPageToPostFromFan().toString());
					outWriter.write((String) baseInfo.get("likes"));
					outWriter.endRecord();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		outWriter.close();

	}



	private static PSAR getIdPSAR(Hashtable<String, PSAR> result, String currID, String string) {
		if (result.get(currID) != null) {
			return result.get(currID);
		}
		PSAR newPSAR = new PSAR(currID, string);
		result.put(currID, newPSAR);
		return newPSAR;
	}


	private static CsvWriter openOutputFile(String outFile) {

		boolean alreadyExists = new File(outFile).exists();

		try {
			// use FileWriter constructor that specifies open for appending
			CsvWriter csvOutput = new CsvWriter(new FileWriter(outFile, true), ',');

			// if the file didn't already exist then we need to write out the header line
			if (!alreadyExists)	{
				csvOutput.write("nome");
				csvOutput.write("id");
				csvOutput.write("post");
				csvOutput.write("fanPost");
				csvOutput.write("comments");
				csvOutput.write("likes");
				csvOutput.write("shares");
				csvOutput.write("commentsToOwnPost");
				csvOutput.write("commentsToOtherPost");
				csvOutput.write("fan");

				csvOutput.endRecord();
			}
			// else assume that the file already has the correct header line
			return csvOutput;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}



}
