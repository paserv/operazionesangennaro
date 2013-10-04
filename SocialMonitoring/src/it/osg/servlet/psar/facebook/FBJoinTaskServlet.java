package it.osg.servlet.psar.facebook;

import it.osg.model.PSARData;
import it.osg.servlet.JoinTaskServlet;
import it.osg.utils.ArrayUtils;
import it.osg.utils.Constants;
import it.osg.utils.DatastoreUtils;
import it.osg.utils.DateUtils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

public class FBJoinTaskServlet extends JoinTaskServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	@Override
	protected long getTimeout() {
		return (Long.valueOf((String) DatastoreUtils.getValue(Constants.SETTINGS_TABLE, "property", "jointimeout", "value")));
	}


	@Override
	protected long getDelay() {
		return Long.valueOf((String) DatastoreUtils.getValue(Constants.SETTINGS_TABLE, "property", "joindelay", "value"));
	}


	@Override
	protected String getSubjectMail(String pageId) {
		return "Dati relativi alla pagina Facebook con ID " + pageId;
	}


	@Override
	protected String getBodyMail(String from, String to, String timestamp, String pageId, long elapsedTime) {
		return "Periodo di riferimento:\nFROM: " + from + "\nTO: " + to + "\nQuery iniziata il: " + DateUtils.parseTimestamp(Long.valueOf(timestamp)) + "\nElapsed Time: " + elapsedTime + "\n\nRisultati per ID = " + pageId;
	}


	@Override
	protected String getAttachFileName(String pageId) {
		return pageId + ".csv";
	}

	@Override
	protected String getJoinTaskName() {
		return "FBpsarjointask";
	}


	@Override
	public String getQueueName() {
		return "default";
	}

	@Override
	protected String getAttachFile(String idTransaction, String from, String to) {


		String dataCSV = "Nome Sindaco;Totale Post from Account;Totale Post from Fan;Totale Comments ai Post;Totale Comments ai Post From Fan;Unique Authors dei Comments ai Post;Totale Likes ai Post from Account;Totale Shares dei Post from Account;Commenti del Sindaco ai Post scritti da lui;Commenti del Sindaco ai Post scritti dai Fan sulla sua bacheca;Delta Fan\n";
		double numGiorni = 0;
		try {
			numGiorni = DateUtils.giorniTraDueDate(DateUtils.parseDateAndTime(from), DateUtils.parseDateAndTime(to));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		ArrayList<PSARData> psarData = DatastoreUtils.getPsarDataFB("task", idTransaction);

		Hashtable<String, PSARData> joinedData = aggregatePsarData(psarData);

		Enumeration<String> keys = joinedData.keys();
		while (keys.hasMoreElements()) {
			String currKey = keys.nextElement();
			PSARData currPsar = joinedData.get(currKey);

			//DATI RICAVATI
//			double mediaPostFromPage = currPsar.postFromPageCount/numGiorni;
//			double mediaPostFromFan = currPsar.postFromFanCount/numGiorni;
//			double commentsPerPost = currPsar.commentsCount/currPsar.postFromPageCount;
			double uniqueAuthors = ArrayUtils.removeDuplicate(currPsar.authors).size();
//			double mediaLikePerPost = currPsar.likesCount/currPsar.postFromPageCount;
//			double sharesPerPost = currPsar.sharesCount/currPsar.postFromPageCount;
//			double commentsPerAuthor = currPsar.commentsCount/uniqueAuthors;
//			double uniqueAuthorsPerPost = uniqueAuthors/currPsar.postFromPageCount;

			//DA CERCARE
			long totNuoviFan = 0; /* ci vuole un cron job attivo che monitora il dato */
			totNuoviFan = getFanCount(currKey, to) - getFanCount(currKey, from);
			//double mediaNuoviFan = 0; /* ci vuole un cron job attivo che monitora il dato */
			
			
//			double totFan = 0;
//			double totTalkAbout = 0;
			//String pageName = "";
//			Hashtable<String, Object> baseInfo = FacebookUtils.getBaseInfo(currKey);
//			if (!((String) baseInfo.get("likes")).equals("")) {
//				totFan = Double.valueOf((String) baseInfo.get("likes"));
//			}
//			if (!((String) baseInfo.get("talking_about_count")).equals("")) {
//				totTalkAbout = Double.valueOf((String) baseInfo.get("talking_about_count"));
//			}
			//pageName = (String) baseInfo.get("name");


			//PRESENTI NEL DB
			//Gi� presenti nel DB
//			String regione = "";
//			String areaISTAT = "";
//			String fasciaEtaISTAT = "";
//			String provincia = "";
//			String sesso = "";
//			String annoNascita = "";
//			String partito = "";
			String sindacoName = "";
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Filter DBInfo = new FilterPredicate("IDFacebook", FilterOperator.EQUAL, currKey);
			Query q = new Query("anagraficaSindaco").setFilter(DBInfo);
			PreparedQuery pq = datastore.prepare(q);
			for (Entity ent : pq.asIterable()) {
//				annoNascita = (String) ent.getProperty("annoNascita");
//				areaISTAT = (String) ent.getProperty("areaISTAT");
//				fasciaEtaISTAT = (String) ent.getProperty("fasciaEtaISTAT");
				sindacoName = (String) ent.getProperty("nome");
//				partito = (String) ent.getProperty("partito");
//				provincia = (String) ent.getProperty("provincia");
//				regione = (String) ent.getProperty("regione");
//				sesso = (String) ent.getProperty("sesso");
			}

//			dataCSV = dataCSV + sindacoName + ";" + pageName + ";" + currKey + ";" + totFan + ";" + totTalkAbout + ";" +
//					regione + ";" + provincia + ";" + sesso + ";" + annoNascita + ";" + partito + ";" +
//					currPsar.postFromPageCount + ";" + currPsar.postFromFanCount + ";" + currPsar.commentsCount + ";" +
//					uniqueAuthors + ";" + currPsar.likesCount + ";" + currPsar.sharesCount + ";" + mediaPostFromPage + ";" +
//					mediaPostFromFan + ";" + commentsPerPost + ";" + uniqueAuthorsPerPost + ";" + mediaLikePerPost + ";" +
//					sharesPerPost + ";" + commentsPerAuthor + ";" + areaISTAT + ";" + fasciaEtaISTAT + ";" +"\n";

			dataCSV = dataCSV + sindacoName + ";" +
					currPsar.postFromPageCount + ";" + currPsar.postFromFanCount + ";" + currPsar.commentsCount + ";" + currPsar.commentsToPostFromFan + ";" + 
					uniqueAuthors + ";" + currPsar.likesCount + ";" + currPsar.sharesCount + ";" + 
					currPsar.commnetsFromPageToPostFromPage + ";" + currPsar.commnetsFromPageToPostFromFan + ";" + totNuoviFan + "\n";
			
		}

		return dataCSV;		
	}


	private Hashtable<String, PSARData> aggregatePsarData(ArrayList<PSARData> psarData) {
		Hashtable<String, PSARData> result = new Hashtable<String, PSARData>();
		Iterator<PSARData> iter = psarData.iterator();
		while (iter.hasNext()) {
			PSARData curr = iter.next();
			if (result.containsKey(curr.pageId)) {
				PSARData alreadyPresentData = result.get(curr.pageId);
				alreadyPresentData.commentsCount = alreadyPresentData.commentsCount + curr.commentsCount;
				alreadyPresentData.likesCount = alreadyPresentData.likesCount + curr.likesCount;
				alreadyPresentData.postFromFanCount = alreadyPresentData.postFromFanCount + curr.postFromFanCount;
				alreadyPresentData.postFromPageCount = alreadyPresentData.postFromPageCount + curr.postFromPageCount;
				alreadyPresentData.sharesCount = alreadyPresentData.sharesCount + curr.sharesCount;
				alreadyPresentData.authors.addAll(curr.authors);
				
				alreadyPresentData.commentsToPostFromFan = alreadyPresentData.commentsToPostFromFan + curr.commentsToPostFromFan;
				alreadyPresentData.commnetsFromPageToPostFromFan = alreadyPresentData.commnetsFromPageToPostFromFan + curr.commnetsFromPageToPostFromFan;
				alreadyPresentData.commnetsFromPageToPostFromPage = alreadyPresentData.commnetsFromPageToPostFromPage + curr.commnetsFromPageToPostFromPage;
				
				
			} else {
				result.put(curr.pageId, curr);
			}
		}
		return result;
	}

	private long getFanCount (String idPage, String date) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q;
		try {
			Filter fromFilter = new FilterPredicate("date", FilterOperator.GREATER_THAN_OR_EQUAL, DateUtils.parseDateAndTime(date));
			Filter idPageFilter = new FilterPredicate("idFacebook", FilterOperator.EQUAL, idPage);
			Filter compositeFilter = CompositeFilterOperator.and(idPageFilter, fromFilter);
			q = new Query(Constants.FACEBOOK_MONITOR_TABLE).setFilter(compositeFilter).addSort("date", SortDirection.ASCENDING);
			PreparedQuery pq = datastore.prepare(q);
			long fanCount;
			for (Entity ent : pq.asIterable()) {
				if ( ent.getProperty("like_count") != null) {
					return (Long) ent.getProperty("like_count");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0L;
	}


}
