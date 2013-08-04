package it.osg.servlet.baseinfo;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import it.osg.servlet.JoinTaskServlet;
import it.osg.utils.DatastoreUtils;
import it.osg.utils.DateUtils;

public class BaseInfoJoinTaskServlet extends JoinTaskServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected long getTimeout() {
		return (Long) DatastoreUtils.getValue("conf", "property", "jointimeout", "value");
	}


	@Override
	protected long getDelay() {
		return (Long) DatastoreUtils.getValue("conf", "property", "joindelay", "value");
	}


	@Override
	protected String getSubjectMail(String pageId) {
		return "Dati relativi alla pagina con ID " + pageId;
	}


	@Override
	protected String getBodyMail(String from, String to, String timestamp, String pageId, long elapsedTime) {
		return "Periodo di riferimento:\nFROM: " + from + "\nTO: " + to + "\nQuery iniziata il: " + DateUtils.parseTimestamp(Long.valueOf(timestamp)) + "\nElapsed Time: " + elapsedTime + "\n\nRisultati per ID Facebook = " + pageId;
	}


	@Override
	protected String getAttachFileName(String pageId) {
		return pageId + ".csv";
	}


	@Override
	protected String getAttachFile(String idTransaction) {
		String result = "ID;Nome;TotalFan;TalkingAbout;FirstPost;LastPost\n";
		DatastoreService DS = DatastoreServiceFactory.getDatastoreService();
		Query q;
		PreparedQuery pq;
		Filter idFilter = new FilterPredicate("idTransaction", FilterOperator.EQUAL, idTransaction);
		q = new Query(BaseInfoSubTaskServlet.subtasktable).setFilter(idFilter);
		pq = DS.prepare(q);
		for (Entity ent : pq.asIterable()) {
			result = result + ent.getProperty("pageId") + ";" + ent.getProperty("nome") + ";" + ent.getProperty("likes") + ";" + ent.getProperty("talking_about_count") + ";" + ent.getProperty("startdate")  + ";" + ent.getProperty("enddate") + "\n";
		}
		//TODO Delete BaseInfoSubTaskServlet.subtasktable Entities
		return result;		
	}


	@Override
	protected String getJoinTaskName() {
		return "baseinfojointask";
	}


	@Override
	public String getQueueName() {
		return "baseinfo";
	}



}
