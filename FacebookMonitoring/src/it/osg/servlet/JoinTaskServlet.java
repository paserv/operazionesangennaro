package it.osg.servlet;

import it.osg.service.model.Graph;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;
import it.osg.utils.Utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class JoinTaskServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		//INPUT DATA
		String numTask = req.getParameter("numTask");
		String idTransaction = req.getParameter("idTransaction");
		String from = req.getParameter("from");
		String to = req.getParameter("to");
		String mail = req.getParameter("mail");
		String timestamp = req.getParameter("timestamp");
		String pageId = req.getParameter("pageId");

		//CHECK FINE TASKS
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q;
		PreparedQuery pq;
		//Filter idFilter = new FilterPredicate("date", FilterOperator.EQUAL, idTransaction);
		q = new Query(idTransaction);
		pq = datastore.prepare(q);
		int executedTask = pq.countEntities();
		if (executedTask == Integer.valueOf(numTask)) {
			/*
			 * AGGREGA DATI
			 */
			//INPUT DATA
			double numGiorni = 0;
			Date f = null;
			Date t = null;
			try {
				f = DateUtils.parseDateAndTime(from);
				t = DateUtils.parseDateAndTime(to);
				numGiorni = DateUtils.giorniTraDueDate(f, t);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//OUTPUT DATA
			//Da aggregare
			double totPostFromPage = 0;
			double totPostFromFan = 0;
			double totComments = 0;
			String authors = "";
			double uniqueAuthors = 0;
			double totLikes = 0;
			double totShares = 0;
			//da ricavare
			double mediaPostFromPage;
			double mediaPostFromFan;
			double commentsPerPost;
			double uniqueAuthorsPerPost;
			double mediaLikePerPost;
			double sharesPerPost;
			double commentsPerAuthor;			
			//Da cercare
			double totNuoviFan = 0;
			double mediaNuoviFan = 0;
			double totFan;
			double totTalkAbout;
			String pageName;
			//Già presenti nel DB
			String regione;
			String provincia;
			String sesso;
			String annoNascita;
			String partito;
			String URL;
			String tipologiaAccount;


			for (Entity ent : pq.asIterable()) {
				totPostFromPage = totPostFromPage + (Double) ent.getProperty("totParzPostFromPage");
				totPostFromFan = totPostFromFan + (Double) ent.getProperty("totParzPostFromFan");
				totComments = totComments + (Double) ent.getProperty("totParzComments");
				authors = authors + (String) ent.getProperty("authors");
				totLikes = totLikes + (Double) ent.getProperty("totParzLikes");
				totShares = totShares + (Double) ent.getProperty("totParzShares");
			}

			mediaPostFromPage = totPostFromPage/numGiorni;
			mediaPostFromFan = totPostFromFan/numGiorni;
			commentsPerPost = totComments/totPostFromPage;
			uniqueAuthors = Utils.getUniqueAuthors(authors);
			mediaLikePerPost = totLikes/totPostFromPage;
			sharesPerPost = totShares/totPostFromPage;
			commentsPerAuthor = totComments/uniqueAuthors;
			
			Hashtable<String, Object> baseInfo = FacebookUtils.getBaseInfo(pageId);
			totFan = Double.valueOf((String) baseInfo.get("likes"));
			totTalkAbout = Double.valueOf((String) baseInfo.get("talking_about_count"));
			pageName = (String) baseInfo.get("pageName");
			
			//GET INFO PAGE ID
			
			
			
			//SEND MAIL

		} else {
			//RIMETTE IN CODA SE STESSO 
			Queue queue = QueueFactory.getDefaultQueue();
			//Queue queue = QueueFactory.getQueue("subscription-queue");
			queue.add(TaskOptions.Builder.withUrl("/jointask").param("numTask", numTask).param("idTransaction", idTransaction).param("from", from).param("to", to).param("mail", mail).param("timestamp", timestamp).param("pageId", pageId));
		}




	}



	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}


}
