package it.osg.servlet.psar.youtube;

import it.osg.model.PSARData;
import it.osg.servlet.JoinTaskServlet;
import it.osg.utils.Constants;
import it.osg.utils.DatastoreUtils;
import it.osg.utils.DateUtils;
import it.osg.utils.YouTubeUtils;

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
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class YOJoinTaskServlet extends JoinTaskServlet {

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
		return "Dati relativi alla pagina Youtube con ID " + pageId;
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
		return "YOpsarjointask";
	}


	@Override
	public String getQueueName() {
		return "default";
	}

	@Override
	protected String getAttachFile(String idTransaction, String from, String to) {

		//TODO
		String dataCSV = "Nome Sindaco;ID YouTube;Regione;Provincia;Sesso;Anno di Nascita;Partito;Totale Views;Totale Subscribers;Apertura Account;Activities;Views;Likes;DisLikes;Favourites;Comments;Area ISTAT;Fascia Eta ISTAT\n";
		double numGiorni = 0;
		try {
			numGiorni = DateUtils.giorniTraDueDate(DateUtils.parseDateAndTime(from), DateUtils.parseDateAndTime(to));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		ArrayList<PSARData> psarData = DatastoreUtils.getPsarDataYT("task", idTransaction);

		Hashtable<String, PSARData> joinedData = aggregatePsarData(psarData);

		Enumeration<String> keys = joinedData.keys();
		while (keys.hasMoreElements()) {
			String currKey = keys.nextElement();
			PSARData currPsar = joinedData.get(currKey);

			//DATI RICAVATI
			double mediaComments = currPsar.commentsCount/numGiorni;
			double mediaLikes = currPsar.likesCount;
			double likesPerActivity = currPsar.likesCount/currPsar.postFromPageCount;
			double commentsPerActivity = currPsar.commentsCount/currPsar.postFromPageCount;
			
			//DA CERCARE
			String subscribers = "";
			String views = "";
			String joineddate = "";
			Hashtable<String, String> baseInfo = YouTubeUtils.getBaseInfo(currKey);
			subscribers = baseInfo.get("subscribers");
			views = baseInfo.get("views");
			joineddate = baseInfo.get("joineddate");
			
			
			//PRESENTI NEL DB
			//Già presenti nel DB
			String regione = "";
			String areaISTAT = "";
			String fasciaEtaISTAT = "";
			String provincia = "";
			String sesso = "";
			String annoNascita = "";
			String partito = "";
			String sindacoName = "";
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Filter DBInfo = new FilterPredicate("IDYoutube", FilterOperator.EQUAL, currKey);
			Query q = new Query("anagraficaSindaco").setFilter(DBInfo);
			PreparedQuery pq = datastore.prepare(q);
			for (Entity ent : pq.asIterable()) {
				annoNascita = (String) ent.getProperty("annoNascita");
				areaISTAT = (String) ent.getProperty("areaISTAT");
				fasciaEtaISTAT = (String) ent.getProperty("fasciaEtaISTAT");
				sindacoName = (String) ent.getProperty("nome");
				partito = (String) ent.getProperty("partito");
				provincia = (String) ent.getProperty("provincia");
				regione = (String) ent.getProperty("regione");
				sesso = (String) ent.getProperty("sesso");
			}

			dataCSV = dataCSV + sindacoName + ";" + currKey + ";" +
					regione + ";" + provincia + ";" + sesso + ";" + annoNascita + ";" + partito + ";" +
					views + ";" + subscribers + ";" + joineddate + ";" +
					currPsar.postFromPageCount + ";" + currPsar.viewCount + ";" + currPsar.likesCount + ";" + currPsar.dislikesCount + ";" +
					+ currPsar.favouriteCount + ";" + currPsar.commentsCount + ";" + areaISTAT + ";" + fasciaEtaISTAT + ";" +"\n";

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
				alreadyPresentData.dislikesCount = alreadyPresentData.dislikesCount + curr.dislikesCount;
				alreadyPresentData.postFromPageCount = alreadyPresentData.postFromPageCount + curr.postFromPageCount;
				alreadyPresentData.favouriteCount = alreadyPresentData.favouriteCount + curr.favouriteCount;
				alreadyPresentData.viewCount = alreadyPresentData.viewCount + curr.viewCount;
			} else {
				result.put(curr.pageId, curr);
			}
		}
		return result;
	}



}
