package it.osg.servlet.imm2;

import it.osg.utils.ArrayUtils;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;
import it.osg.utils.MailUtils;
import it.osg.utils.Utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class Imm2JoinTaskServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final long timeout = 1000000L;


	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		//INPUT DATA
		String numTask = req.getParameter("numTask");
		String idTransaction = req.getParameter("idTransaction");
		String from = req.getParameter("from");
		String to = req.getParameter("to");
		String mail = req.getParameter("mail");
		String timestamp = req.getParameter("timestamp");
		String pageId = req.getParameter("pageId");

		long elapsedTime = (System.currentTimeMillis() - Long.valueOf(timestamp))/1000;

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q;
		PreparedQuery pq;
		Filter idFilter = new FilterPredicate("idTransaction", FilterOperator.EQUAL, idTransaction);

		if (isTransactionEnded(idTransaction, numTask)) {

			String dataCSV = "";

			double numGiorni = 0;
			try {
				numGiorni = DateUtils.giorniTraDueDate(DateUtils.parseDateAndTime(from), DateUtils.parseDateAndTime(to));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			ArrayList<String> sindaci = new ArrayList<String>();
			if (pageId.equalsIgnoreCase("all")) {
				sindaci = Utils.getAllSindaci();
			} else {
				sindaci.add(pageId);
			}


			Iterator<String> iterSindaci = sindaci.iterator();
			while (iterSindaci.hasNext()) {
				String currSindaco = iterSindaci.next();

				//OUTPUT DATA
				//Da aggregare
				double totPostFromPage = 0;
				double totPostFromFan = 0;
				double totComments = 0;
				ArrayList<String> authors = new ArrayList<String>();
				double uniqueAuthors = 0;
				double totLikes = 0;
				double totShares = 0;
				//da ricavare
				double mediaPostFromPage = 0;
				double mediaPostFromFan = 0;
				double commentsPerPost = 0;
				double uniqueAuthorsPerPost = 0;
				double mediaLikePerPost = 0;
				double sharesPerPost = 0;
				double commentsPerAuthor = 0;			
				//Da cercare
				double totNuoviFan = 0; /* ci vuole un cron job attivo che monitora il dato */
				double mediaNuoviFan = 0; /* ci vuole un cron job attivo che monitora il dato */
				double totFan = 0;
				double totTalkAbout = 0;
				String pageName = "";
				//Già presenti nel DB
				String regione = "";
				String provincia = "";
				String sesso = "";
				long annoNascita = 0L;
				String partito = "";
				String URL = "";
				String tipologiaAccount = "";
				String sindacoName = "";

				//Query sindaco corrente
				Filter sindacoFilter = new FilterPredicate("pageId", FilterOperator.EQUAL, currSindaco);
				Filter compositeFilter = CompositeFilterOperator.and(idFilter, sindacoFilter);
				q = new Query("task").setFilter(compositeFilter);
				pq = datastore.prepare(q);
				//AGGREGA DATI
				for (Entity ent : pq.asIterable()) {
					totPostFromPage = totPostFromPage + (Double) ent.getProperty("totParzPostFromPage");
					totPostFromFan = totPostFromFan + (Double) ent.getProperty("totParzPostFromFan");
					totComments = totComments + (Double) ent.getProperty("totParzComments");
					authors.addAll(ArrayUtils.splitAndAdd(((Text) ent.getProperty("authors")).getValue(), ","));
					totLikes = totLikes + (Double) ent.getProperty("totParzLikes");
					totShares = totShares + (Double) ent.getProperty("totParzShares");

					//RICAVA DATI
					mediaPostFromPage = totPostFromPage/numGiorni;
					mediaPostFromFan = totPostFromFan/numGiorni;
					commentsPerPost = totComments/totPostFromPage;
					uniqueAuthors = ArrayUtils.removeDuplicate(authors).size();
					mediaLikePerPost = totLikes/totPostFromPage;
					sharesPerPost = totShares/totPostFromPage;
					commentsPerAuthor = totComments/uniqueAuthors;
					uniqueAuthorsPerPost = uniqueAuthors/totPostFromPage;

					//DA CERCARE
					Hashtable<String, Object> baseInfo = FacebookUtils.getBaseInfo(currSindaco);
					if (!((String) baseInfo.get("likes")).equals("")) {
						totFan = Double.valueOf((String) baseInfo.get("likes"));
					}
					if (!((String) baseInfo.get("talking_about_count")).equals("")) {
						totTalkAbout = Double.valueOf((String) baseInfo.get("talking_about_count"));
					}
					
					pageName = (String) baseInfo.get("pageName");

					//PRESENTI NEL DB
					Filter DBInfo = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, KeyFactory.createKey("anagraficaSindaco", currSindaco));
					q = new Query("anagraficaSindaco").setFilter(DBInfo);
					pq = datastore.prepare(q);
					for (Entity ent2 : pq.asIterable()) {
						regione = (String) ent2.getProperty("regione");
						provincia = (String) ent2.getProperty("provincia");
						sesso = (String) ent2.getProperty("sesso");
						annoNascita = (Long) ent2.getProperty("annoNascita");
						partito = (String) ent2.getProperty("partito");
						URL = (String) ent2.getProperty("URL");
						tipologiaAccount = (String) ent2.getProperty("tipologiaAccount");
						sindacoName = (String) ent2.getProperty("sindaco");

					}

				}

				dataCSV = dataCSV + sindacoName + "," + pageName  + "," + currSindaco  + "," + totFan  + "," + totTalkAbout + "," + regione + "," + provincia + "," + sesso + "," + annoNascita + "," + partito + "," + URL + "," + tipologiaAccount +
						"," + totPostFromPage + "," + totPostFromFan + "," + totComments + "," + uniqueAuthors + "," + totLikes + "," + totShares +
						"," + mediaPostFromPage  + "," + mediaPostFromFan  + "," + commentsPerPost + "," + uniqueAuthorsPerPost + "," + mediaLikePerPost  + "," + sharesPerPost  + "," + commentsPerAuthor + "\n";	
			}


			//INVIA MAIL
			String bodyMail = "Periodo di riferimento:\nFROM: " + from + "\nTO: " + to + "\nQuery iniziata il: " + DateUtils.parseTimestamp(Long.valueOf(timestamp)) + "\nElapsed Time: " + elapsedTime + "\n\nRisultati per ID Facebook = " + pageId;
			String headerCSV = "Nome Sindaco,Nome Pagina,ID Facebook,Totale Fan,Totale TalkAbout,Regione,Provincia,Sesso,Anno di Nascita,Partito,URL Facebook,Tipologia Account,Totale Post from Account,Totale Post from Fan,Totale Comments ai Post,Unique Authors dei Comments ai Post,Totale Likes ai Post from Account,Totale Shares dei Post from Account,Media Post from Account al giorno,Media Post from Fan al giorno,Media Comments per Post from Account,Media Unique Authors per Post from Account,Media Like per Post from Account,Media Shares per Post,Media Comments per Author\n";
			String attachFile = headerCSV + dataCSV;
			String subject = "Dati relativi alla pagina con ID " + pageId;
			MailUtils.sendMail(mail, subject, bodyMail, pageId + ".csv", attachFile);



		} else {
			if (timeout - elapsedTime > 0) {
				//RIMETTE IN CODA SE STESSO DOPO UN CERTO DELAY
				try {
					Thread.sleep(100000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//TASK CHE MONITORA GLI ALTRI TASK (JOINTASKSERVLET)
				Queue queue = QueueFactory.getDefaultQueue();
				queue.add(TaskOptions.Builder.withUrl("/jointask").param("numTask", numTask).param("idTransaction", idTransaction).param("from", from).param("to", to).param("mail", mail).param("timestamp", timestamp).param("pageId", pageId));	
			} else {
				//TODO SALVA IL SALVABILE ED INVIA LA MAIL
			}
		}




	}


	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}



	


	private boolean isTransactionEnded (String idTransaction, String numTask) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q;
		PreparedQuery pq;
		Filter idFilter = new FilterPredicate("idTransaction", FilterOperator.EQUAL, idTransaction);
		q = new Query("task").setFilter(idFilter);
		pq = datastore.prepare(q);
		int executedTask = pq.countEntities();
		if (Integer.valueOf(numTask) == executedTask) return true;
		else return false;
	}


}
