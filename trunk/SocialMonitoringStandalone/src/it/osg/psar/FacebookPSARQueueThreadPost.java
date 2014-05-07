package it.osg.psar;

import facebook4j.Post;
import it.osg.data.PSAR;
import it.osg.queue.Queue;
import it.osg.queue.QueueThreadImpl;
import it.osg.queue.QueueThreadImpl2;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;

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


public class FacebookPSARQueueThreadPost {

	public static String inputFile = "resources/facebookIDS.csv";
	public static char inputCharDelimiter = ';';

	public static String outputPath = "resources/facebookModa";
	public static String from = "01-01-2014 00:00:00";
	public static String to = "15-01-2014 23:59:59";

	public static String idField = "pageID";
	public static String nomeField = "nome";

	public static int QUEUELENGHT = 50; //lunghezza coda
	public static long THREAD_SLEEP_INTERVAL = 5000L; //tempo massimo che deve aspettare ogni thread se non trova posto nella coda prima di ritentare l'inserimento nella coda
	public static long QUEUE_SLEEP_INTERVAL = 5000L; //tempo dopo il quale la coda esegue il check per capire se il timeout è stato superato
	public static long QUEUE_TIMEOUT = 1000L; //timeout della coda

	public static void main(String[] args) {
		Queue queue = new Queue(QUEUELENGHT, THREAD_SLEEP_INTERVAL, QUEUE_SLEEP_INTERVAL);

		PSAR myPSAR = new PSAR("userID", "userName");
		for (int i = 0; i < 100; i++) {
			QueueThreadImpl2 worker = new QueueThreadImpl2("userID", String.valueOf(i), myPSAR);
			worker.setName("name_" + i);
			worker.addToQueue(queue);
		}

		queue.execute();
		long start = System.currentTimeMillis();
		queue.awaitQueueTermination(QUEUE_TIMEOUT);
		long end = System.currentTimeMillis();
		System.out.println("Elapsed Time: " + (end - start)/1000 + " seconds");
	

		System.out.println("Num Likes: " + myPSAR.getLikes());

	}

	public static void main2(String[] args) {

		long start = System.currentTimeMillis();

		CsvWriter outWriter = openOutputFile(outputPath + "_out_" + System.currentTimeMillis() + ".csv");

		Queue queue = new Queue(10, 5000L, 5000L);

		Hashtable<String, String> ids = getInputAccounts(inputFile, idField, nomeField, inputCharDelimiter);
		Hashtable<String, PSAR> result = new Hashtable<String, PSAR>();

		//GET TO DATE
		String toDay = to.substring(0, 10) + " 23:59:59";
		Date f = null;
		Date t = null;
		try {
			f = DateUtils.parseDateAndTime(from);
			t = DateUtils.parseDateAndTime(toDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Enumeration<String> keys = ids.keys();
		while (keys.hasMoreElements()) {
			String currID = keys.nextElement();
			System.out.println("nome: " + ids.get(currID));
			PSAR idPSAR = getIdPSAR(result, currID, ids.get(currID));

			//Get all Post
			ArrayList<Post> posts = new ArrayList<Post>();
			posts =	FacebookUtils.getAllPosts(currID, f, t, new String[]{"id"});

			//SPLITTO 1 POST ALLA VOLTA ED INSERISCO UNA QUEUE DI THREAD
			Iterator<Post> iter = posts.iterator();
			while (iter.hasNext()) {
				Post currPost = iter.next();
				QueueThreadImpl worker = new QueueThreadImpl(currID, currPost.getId(), idPSAR);
				worker.setName(currPost.getId());
				worker.addToQueue(queue);
			}	

		}

		queue.execute();
		queue.awaitQueueTermination(60L);

		Enumeration<PSAR> elements = result.elements();
		while (elements.hasMoreElements()) {
			PSAR curr = elements.nextElement();
			try {
				outWriter.write(curr.getId());
				outWriter.write(curr.getNome());
				outWriter.write(curr.getPostFromPage().toString());
				outWriter.write(curr.getPostFromFan().toString());
				outWriter.write(curr.getComments().toString());
				outWriter.write(curr.getLikes().toString());
				outWriter.write(curr.getShares().toString());
				outWriter.write(curr.getCommentsToPostFromFan().toString());
				outWriter.write(curr.getCommnetsFromPageToPostFromPage().toString());
				outWriter.write(curr.getCommnetsFromPageToPostFromFan().toString());
				outWriter.endRecord();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		outWriter.close();

		long end = System.currentTimeMillis();
		System.out.println("Elapsed Time: " + (end - start)/1000 + " seconds");

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


	private static Hashtable<String, String> getInputAccounts(String inFile, String idField2, String nomeField2, char inputCharDelimiter2) {

		Hashtable<String, String> result = new Hashtable<String, String>();

		CsvReader idFile;
		try {
			idFile = new CsvReader(inFile);
			idFile.setDelimiter(inputCharDelimiter2);
			idFile.readHeaders();

			while (idFile.readRecord())	{
				String accountID = idFile.get(idField2);
				String name = idFile.get(nomeField2);
				result.put(accountID, name);
				System.out.println(accountID + ":" + name);
			}

			idFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}


}
