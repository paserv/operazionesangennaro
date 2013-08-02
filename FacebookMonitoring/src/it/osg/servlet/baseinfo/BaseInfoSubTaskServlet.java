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
		Date endDate = null;
		Date f = null;
		Date t = null;

		Date startFB = null;
		try {
			startFB = DateUtils.parseDateAndTime("01-02-2004 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		

		
		//FIRST POST
		f = startFB;
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

		
		
		
		//LAST POST
		t = DateUtils.getNowDate();
		f = DateUtils.addMonthToDate(t, -1);
		
		while (true) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			ResponseList<Post> facResults2;
			try {
				facResults2 = FacebookUtils.getFB().getFeed(pageId, new Reading().since(f).until(t).fields("created_time").limit(1));
				if (facResults2 != null && facResults2.size() != 0) {
					Post currPost = facResults2.get(0);
					endDate = currPost.getCreatedTime();
					break;
				} else if (DateUtils.diffInDay(startFB, f) > 0) {
					t = f;
					f = DateUtils.addMonthToDate(t, -1);
				} else if (DateUtils.diffInDay(startFB, f) < 0) {
					startDate =  startFB;
					break;
				}
			} catch (FacebookException e) {
				e.printStackTrace();
			}
			
		}
		
		Hashtable<String, Object> bi = FacebookUtils.getBaseInfo(pageId);

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Entity ent = new Entity(subtasktable);
		ent.setProperty("idTransaction", idTranscation);
		ent.setProperty("pageId", pageId);
		ent.setUnindexedProperty("name", bi.get("name"));
		ent.setUnindexedProperty("likes", bi.get("likes"));
		ent.setUnindexedProperty("talking_about_count", bi.get("talking_about_count"));
		ent.setUnindexedProperty("startdate", DateUtils.formatDate(startDate));
		ent.setUnindexedProperty("enddate", DateUtils.formatDate(endDate));
		datastore.put(ent);
		txn.commit();
		
	}


}
