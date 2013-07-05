package it.osg.servlet.mapreduce;

import it.osg.utils.ArrayUtils;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class JoinTaskServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final long timeout = 400000L;


	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		//INPUT DATA
		String numTask = req.getParameter("numTask");/* che è uguale al numero giorni */
		String idTransaction = req.getParameter("idTransaction");
		String from = req.getParameter("from");
		String to = req.getParameter("to");
		String mail = req.getParameter("mail");
		String timestamp = req.getParameter("timestamp");
		String pageId = req.getParameter("pageId");

		long elapsedTime = (System.currentTimeMillis() - Long.valueOf(timestamp))/1000;

		//CHECK FINE TASKS
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q;
		PreparedQuery pq;
		Filter idFilter = new FilterPredicate("idTransaction", FilterOperator.EQUAL, idTransaction);
		q = new Query("task").setFilter(idFilter);
		pq = datastore.prepare(q);
		int executedTask = pq.countEntities();
		if (executedTask == Integer.valueOf(numTask)) {

			//INPUT DATA
			double numGiorni = Double.valueOf(numTask);
			//			Date f = null;
			//			Date t = null;
			//			try {
			//				f = DateUtils.parseDateAndTime(from);
			//				t = DateUtils.parseDateAndTime(to);
			//				numGiorni = DateUtils.giorniTraDueDate(f, t);
			//			} catch (ParseException e) {
			//				e.printStackTrace();
			//			}

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
			String annoNascita = "";
			String partito = "";
			String URL = "";
			String tipologiaAccount = "";


			//AGGREGA DATI
			for (Entity ent : pq.asIterable()) {
				totPostFromPage = totPostFromPage + (Double) ent.getProperty("totParzPostFromPage");
				totPostFromFan = totPostFromFan + (Double) ent.getProperty("totParzPostFromFan");
				totComments = totComments + (Double) ent.getProperty("totParzComments");
				authors.addAll(ArrayUtils.splitAndAdd(((Text) ent.getProperty("authors")).getValue(), ","));
				totLikes = totLikes + (Double) ent.getProperty("totParzLikes");
				totShares = totShares + (Double) ent.getProperty("totParzShares");
			}

			//RICAVA DATI
			mediaPostFromPage = totPostFromPage/numGiorni;
			mediaPostFromFan = totPostFromFan/numGiorni;
			commentsPerPost = totComments/totPostFromPage;
			uniqueAuthors = ArrayUtils.removeDuplicate(authors).size();
			mediaLikePerPost = totLikes/totPostFromPage;
			sharesPerPost = totShares/totPostFromPage;
			commentsPerAuthor = totComments/uniqueAuthors;

			//DA CERCARE
			Hashtable<String, Object> baseInfo = FacebookUtils.getBaseInfo(pageId);
			if (!((String) baseInfo.get("likes")).equals("")) {
				totFan = Double.valueOf((String) baseInfo.get("likes"));
			}
			if (!((String) baseInfo.get("talking_about_count")).equals("")) {
				totTalkAbout = Double.valueOf((String) baseInfo.get("talking_about_count"));
			}
			pageName = (String) baseInfo.get("pageName");

			//PRESENTI NEL DB
			idFilter = new FilterPredicate("Id", FilterOperator.EQUAL, pageId);
			q = new Query("sindaco").setFilter(idFilter);
			pq = datastore.prepare(q);
			for (Entity ent : pq.asIterable()) {
				regione = (String) ent.getProperty("regione");
				provincia = (String) ent.getProperty("provincia");
				sesso = (String) ent.getProperty("sesso");
				annoNascita = (String) ent.getProperty("annoNascita");
				partito = (String) ent.getProperty("partito");
				URL = (String) ent.getProperty("URL");
				tipologiaAccount = (String) ent.getProperty("tipologiaAccount");
			}

			//CREA PARTI MAIL
			String bodyMail = "Periodo di riferimento:\nFROM: " + from + "\nTO: " + to + "\nQuery iniziata il: " + DateUtils.parseTimestamp(Long.valueOf(timestamp)) + "\nElapsed Time: " + elapsedTime + "\n\nRisultati per la pagina " + pageName + "\nID Facebook = " + pageId;

			String attachFile = "Nome Pagina,ID Facebook,Totale Fan,Totale TalkAbout,Regione,Provincia,Sesso,Anno di Nascita,Partito,URL Facebook,Tipologia Account,Totale Post from Account,Totale Post from Fan,Totale Comments ai Post,Unique Authors dei Comments ai Post,Totale Likes ai Post from Account,Totale Shares dei Post from Account,Media Post from Account al giorno,Media Post from Fan al giorno,Media Comments per Post from Account,Media Unique Authors per Post from Account,Media Like per Post from Account,Media Shares per Post,Media Comments per Author" + 
					"\n" + pageName  + "," + pageId  + "," + totFan  + "," + totTalkAbout + "," + regione + "," + provincia + "," + sesso + "," + annoNascita + "," + partito + "," + URL + "," + tipologiaAccount +
					"," + totPostFromPage + "," + totPostFromFan + "," + totComments + "," + uniqueAuthors + "," + totLikes + "," + totShares +
					"," + mediaPostFromPage  + "," + mediaPostFromFan  + "," + commentsPerPost + "," + uniqueAuthorsPerPost + "," + mediaLikePerPost  + "," + sharesPerPost  + "," + commentsPerAuthor;

			//SEND MAIL
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			Message msg = new MimeMessage(session);
			try {
				msg.setFrom(new InternetAddress("donpablooooo@gmail.com", "DONPABLOWATCH"));
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));
				msg.setSubject("Dati relativi alla pagina di " + pageName);
				//msg.setText(bodyMail);

				Multipart mp = new MimeMultipart();
				MimeBodyPart htmlPart = new MimeBodyPart();
				htmlPart.setContent(bodyMail, "text/plain");
				mp.addBodyPart(htmlPart);

				MimeBodyPart attachment = new MimeBodyPart();
				ByteArrayDataSource src = new ByteArrayDataSource(attachFile.getBytes(), "text/plain"); 
				attachment.setFileName(pageName + ".txt");
				attachment.setDataHandler(new DataHandler (src));
				mp.addBodyPart(attachment);

				msg.setContent(mp);
				msg.saveChanges();
				Transport.send(msg);

			} catch (AddressException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}	

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


			}			
		}




	}



	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}


}
