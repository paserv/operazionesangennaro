package it.osg.psar;

import facebook4j.Post;
import it.osg.data.PSAR;
import it.osg.runnable.RunnableQueueImpl;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;
import it.queue.Queue;
import it.queue.TimeoutException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;


public class FacebookPSARQueueThreadPostKeywords {

	public static String resourcesFolder = "resources/";
	public static String outputFolder = "output/";
	public static char inputCharDelimiter = ';';
	
	public static String inputFile = "quotidiani.csv";
	public static String from = "01-01-2014 00:00:00";
	public static String to = "15-01-2014 23:59:59";

	public static int QUEUELENGHT = 50; //Numero massimo thread in stato running
	public static long QUEUE_CHECK_SLEEP = 5000L; //tempo dopo il quale viene eseguito il check per capire: 1)se il timeout � stato superato; 2)se pu� far partire nuovi thread prelevandoli dalla coda
	public static long QUEUE_TIMEOUT = 1000000000L; //timeout della coda

	public static void main(String[] args) {
		
//		System.getProperties().put("http.proxyHost", "proxy.gss.rete.poste");
//		System.getProperties().put("http.proxyPort", "8080");
//		System.getProperties().put("http.proxyUser", "rete\\servill7");
//		System.getProperties().put("http.proxyPassword", "Paolos10");

		CsvWriter outWriter = openOutputFile(outputFolder + "FB_" + from.substring(0, 10) + "TO" + to.substring(0,10) + ".csv");

		Hashtable<String, PSAR> result = new Hashtable<String, PSAR>();

//		//GET TO DATE
//		String toDay = to.substring(0, 10) + " 23:59:59";
//		Date f = null;
//		Date t = null;
//		try {
//			f = DateUtils.parseDateAndTime(from);
//			t = DateUtils.parseDateAndTime(toDay);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//
//		Enumeration<String> keys = ids.keys();
//		while (keys.hasMoreElements()) {
//			String currID = keys.nextElement();
//			System.out.println("nome: " + ids.get(currID));
//			PSAR idPSAR = getIdPSAR(result, currID, ids.get(currID));
//
//			//Get all Post
//			ArrayList<Post> posts = new ArrayList<Post>();
//			posts =	FacebookUtils.getAllPosts(currID, f, t, new String[]{"id"});
//
//			//SPLITTO 1 POST ALLA VOLTA ED INSERISCO UNA QUEUE DI THREAD
//			Iterator<Post> iter = posts.iterator();
//			while (iter.hasNext()) {
//				Post currPost = iter.next();
//				RunnableQueueImpl worker = new RunnableQueueImpl(currID, currPost.getId(), idPSAR);
//				worker.setName(currPost.getId());
//			}	
//
//		}
//
//		long start = System.currentTimeMillis();
//		try {
//			queue.executeAndWait();
//		} catch (TimeoutException e1) {
//			e1.printStackTrace();
//		}
//		
//		long end = System.currentTimeMillis();
//		System.out.println("Elapsed Time: " + (end - start)/1000 + " seconds");
//
//		Enumeration<PSAR> elements = result.elements();
//		while (elements.hasMoreElements()) {
//			PSAR curr = elements.nextElement();
//			try {
//				outWriter.write(curr.getId());
//				outWriter.write(curr.getNome());
//				outWriter.write(curr.getPostFromPage().toString());
//				outWriter.write(curr.getPostFromFan().toString());
//				outWriter.write(curr.getComments().toString());
//				outWriter.write(curr.getLikes().toString());
//				outWriter.write(curr.getShares().toString());
//				outWriter.write(curr.getCommentsToPostFromFan().toString());
//				outWriter.write(curr.getCommnetsFromPageToPostFromPage().toString());
//				outWriter.write(curr.getCommnetsFromPageToPostFromFan().toString());
//				outWriter.endRecord();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//		}
//		outWriter.close();

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
				csvOutput.write("id");
				csvOutput.write("nome");
				csvOutput.write("PostFromPage");
				csvOutput.write("PostFromFan");
				csvOutput.write("Comments");
				csvOutput.write("Likes");
				csvOutput.write("Shares");
				csvOutput.write("CommentsToPostFromFan");
				csvOutput.write("CommnetsFromPageToPostFromPage");
				csvOutput.write("CommnetsFromPageToPostFromFan");

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