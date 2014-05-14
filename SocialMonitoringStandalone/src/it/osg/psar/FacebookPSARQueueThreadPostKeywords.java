package it.osg.psar;

import facebook4j.Post;
import it.osg.data.Hashtags;
import it.osg.data.PSAR;
import it.osg.runnable.RunnableQueueImpl;
import it.osg.runnable.RunnableQueueImpl4Hashtags;
import it.osg.utils.FacebookUtils;
import it.queue.Queue;
import it.queue.TimeoutException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.csvreader.CsvWriter;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;


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
			from = args[0];
			to = args[1];
			QUEUELENGHT = Integer.valueOf(args[2]);
			QUEUE_CHECK_SLEEP = Long.valueOf(args[3]);
			QUEUE_TIMEOUT = Long.valueOf(args[4]);
			keywordString = args[5];
			keywordOperator = args[6];
		}

		//GET DATE
		String fromDay = from.substring(0, 10) + " 00:00:00";
		String toDay = to.substring(0, 10) + " 23:59:59";

		/*Instanzio le code*/
		Queue queuePSAR = new Queue(QUEUELENGHT, QUEUE_CHECK_SLEEP, QUEUE_TIMEOUT);
		queuePSAR.setRollback(false);
		Queue queueHashtags = new Queue(QUEUELENGHT, QUEUE_CHECK_SLEEP, QUEUE_TIMEOUT);
		queueHashtags.setRollback(false);

		/*Instanzio gli oggetti funzionali alla scrittura dei file di output*/
		Hashtable<String, PSAR> resultPSAR = new Hashtable<String, PSAR>();
		Hashtags resultHashtags = new Hashtags();

		/*Instanzio i writer dei file di output*/
		String psarFileName = "FB_" + from.substring(0, 10) + "_TO_" + to.substring(0,10) + "_KEY_" + keywordString + "_" + keywordOperator + "_" + System.currentTimeMillis() + ".csv";
		String hashTagFileName = psarFileName.substring(0, psarFileName.length() - 4) + ".hashtag";
		CsvWriter outWriterPSAR = openOutputFile(outputFolder + psarFileName);
		outWriterPSAR.setDelimiter(';');
		CsvWriter outWriterHashtags = null;
		try {
			outWriterHashtags = new CsvWriter(new FileWriter(outputFolder + hashTagFileName, true), ';');
		} catch (IOException e2) {
			e2.printStackTrace();
		} 
		outWriterHashtags.setDelimiter(';');

		/*Gestione dell' AND o OR delle keyword*/
		String[] keywords = keywordString.split(",");
		if (keywordOperator.equalsIgnoreCase("AND")) {
			keywords = new String[1];
			keywords[0] = keywordString;
		}

		/** Lancio le ricerca sulle keywords, se per esempio
		 * in un post sono presenti 2 keywords,
		 * allora nell'ArrayList sarà presente 2 volte lo stesso autore
		 * nelle cui hashtable di post sarà presente 2 volte lo stesso post */
		ArrayList<Hashtable<String, Hashtable<String, Post>>> postsGroupedByAuthor = new ArrayList<Hashtable<String,Hashtable<String, Post>>>();
		for (int i = 0; i < keywords.length; i++) {
			String currKeyword = keywords[i];
			Hashtable<String, Hashtable<String, Post>> currKeywordPosts = FacebookUtils.getPostByKeyword(currKeyword, fromDay, toDay);
			postsGroupedByAuthor.add(currKeywordPosts);
		}

		/** Qui ri-raggruppo per autore in modo da deduplicare i post */
		Hashtable<String, Hashtable<String, Post>> authorsWithPost = new Hashtable<String, Hashtable<String, Post>>();
		Iterator<Hashtable<String, Hashtable<String, Post>>> iter = postsGroupedByAuthor.iterator();
		while (iter.hasNext()) {
			Hashtable<String, Hashtable<String, Post>> current = iter.next();
			Enumeration<String> keys = current.keys();
			while (keys.hasMoreElements()) {
				String currKey = keys.nextElement();
				if (authorsWithPost.containsKey(currKey)) {	
					Hashtable<String, Post> alreadyExistentPostSet = authorsWithPost.get(currKey);
					alreadyExistentPostSet.putAll(current.get(currKey));					
				} else {
					authorsWithPost.putAll(current);
				}

			}

		}


		/** Per ogni autore e per ogni suo post metto un thread in coda per il calcolo di PSAR
		 *  e un thread in coda per il calcolo delle frequenze degli hashtag */
		Enumeration<String> authors = authorsWithPost.keys();
		while (authors.hasMoreElements()) {
			String currAuthorID = authors.nextElement();
			PSAR idPSAR = getIdPSAR(resultPSAR, currAuthorID, currAuthorID);

			Hashtable<String, Post> posts = authorsWithPost.get(currAuthorID);

			//SPLITTO 1 POST ALLA VOLTA ED INSERISCO UNA QUEUE DI THREAD
			Enumeration<Post> enumPosts = posts.elements();
			while (enumPosts.hasMoreElements()) {
				Post currPost = enumPosts.nextElement();

				/*Aggiungo un worker alla coda degli hashtags*/
				RunnableQueueImpl4Hashtags workerHashtags = new RunnableQueueImpl4Hashtags(currPost, resultHashtags);
				workerHashtags.setName(currPost.getId() + "_ht");
				queueHashtags.addThread(workerHashtags);

				/*Aggiungo un worker alla coda dei PSAR*/
				RunnableQueueImpl workerPSAR = new RunnableQueueImpl(currAuthorID, currPost.getId(), idPSAR);
				workerPSAR.setName(currPost.getId());
				queuePSAR.addThread(workerPSAR);
			}
		}


		/*Avvio le code*/
		long start = System.currentTimeMillis();
		try {
			queuePSAR.executeAndWait();
			queueHashtags.executeAndWait();
		} catch (TimeoutException e1) {
			e1.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("Elapsed Time: " + (end - start)/1000 + " seconds");


		/*File per i risultati di PSAR*/
		Enumeration<PSAR> elements = resultPSAR.elements();
		while (elements.hasMoreElements()) {
			PSAR curr = elements.nextElement();

			Hashtable<String, Object> baseInfo = FacebookUtils.getBaseInfoFromJson(curr.getId());
			if (baseInfo.get("likes") != null && !((String) baseInfo.get("likes")).equals("")) {
				try {
					if (baseInfo.get("name") != null && !((String) baseInfo.get("name")).equals("")) {
						outWriterPSAR.write((String) baseInfo.get("name"));
					} else {
						outWriterPSAR.write(curr.getNome());
					}
					outWriterPSAR.write(curr.getId());
					outWriterPSAR.write(curr.getPostFromPage().toString());
					outWriterPSAR.write(curr.getPostFromFan().toString());
					outWriterPSAR.write(curr.getComments().toString());
					outWriterPSAR.write(curr.getLikes().toString());
					outWriterPSAR.write(curr.getShares().toString());
					outWriterPSAR.write(curr.getCommnetsFromPageToPostFromPage().toString());				
					outWriterPSAR.write(curr.getCommnetsFromPageToPostFromFan().toString());
					outWriterPSAR.write((String) baseInfo.get("likes"));
					outWriterPSAR.endRecord();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		outWriterPSAR.close();


		/*File per i risultati Hashtags*/
		List<Entry<String>> sortedHt = sortMultisetPerEntryCount(resultHashtags.getResultHashtags());
		int topHTNumber;
		if (sortedHt.size() < 100) {
			topHTNumber = sortedHt.size();
		} else {
			topHTNumber = 100;
		}

		List<Entry<String>> topHunHT = sortedHt.subList(0, topHTNumber);
		for (int i = 0; i < topHunHT.size(); i++) {
			try {
				outWriterHashtags.write(topHunHT.get(i).getElement());
				outWriterHashtags.write(String.valueOf(topHunHT.get(i).getCount()));
				outWriterPSAR.endRecord();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		outWriterHashtags.close();

	}




	private static <T> List<Entry<T>> sortMultisetPerEntryCount(Multiset<T> multiset) {
		Comparator<Multiset.Entry<T>> occurence_comparator = new Comparator<Multiset.Entry<T>>() {
			public int compare(Multiset.Entry<T> e1, Multiset.Entry<T> e2) {
				return e2.getCount() - e1.getCount();
			}
		};
		List<Entry<T>> sortedByCount = new ArrayList<Entry<T>>(multiset.entrySet());
		Collections.sort(sortedByCount, occurence_comparator);

		return sortedByCount;
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
			CsvWriter csvOutput = new CsvWriter(new FileWriter(outFile, true), ';');

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
