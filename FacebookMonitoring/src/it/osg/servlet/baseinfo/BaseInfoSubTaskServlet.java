package it.osg.servlet.baseinfo;

import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import it.osg.servlet.SubTaskServlet;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;
import java.text.ParseException;
import java.util.Date;
import java.util.Hashtable;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Transaction;

public class BaseInfoSubTaskServlet extends SubTaskServlet  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String subtasktable = "baseinfo";

	@Override
	protected void runSubTask(String idTranscation, String pageId, Date from, Date to) {
		Date startDate = null;

		Date f = null;
		Date t = null;
		try {
			f = DateUtils.parseDateAndTime("01-02-2004 00:00:00");
		} catch (ParseException e2) {
			e2.printStackTrace();
		}
		t = DateUtils.addMonthToDate(f, 1);
		try {
			while (true) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				ResponseList<Post> facResults = FacebookUtils.getFB().getFeed(pageId, new Reading().since(f).until(t).fields("created_time").limit(1));
				if (facResults != null && facResults.size() != 0) {
					Post currPost = facResults.get(0);
					startDate = currPost.getCreatedTime();
					break;
				} else if (DateUtils.diffInDay(t, DateUtils.getNowDate()) > 0) {
					f = t;
					t = DateUtils.addMonthToDate(f, 1);
				} else if (DateUtils.diffInDay(t, DateUtils.getNowDate()) < 0) {
					startDate =  DateUtils.getNowDate();
					break;
				}
			}


		} catch (FacebookException e) {
			e.printStackTrace();
		}

		Hashtable<String, Object> bi = FacebookUtils.getBaseInfo(pageId);

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Entity ent = new Entity(subtasktable);
		ent.setProperty("pageId", pageId);
		ent.setUnindexedProperty("name", bi.get("name"));
		ent.setUnindexedProperty("likes", bi.get("likes"));
		ent.setUnindexedProperty("talking_about_count", bi.get("talking_about_count"));
		ent.setUnindexedProperty("startdate", DateUtils.formatDate(startDate));
		datastore.put(ent);
		txn.commit();
		
	}


}
