package it.osg.servlet.baseinfo.facebook;

import java.text.ParseException;
import java.util.Date;
import java.util.Hashtable;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;

import it.osg.servlet.SubTaskServlet;
import it.osg.utils.Constants;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;

public class FBBaseInfoSubTaskServlet extends SubTaskServlet  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void runSubTask(String idTranscation, String pageId, Date from, Date to) {
		String endDate = "";
		try {
			ResponseList<Post> posts = FacebookUtils.getFB().getFeed(pageId, new Reading().since(from).until(to).fields("created_time"));
			if (posts != null && posts.size() != 0) {
				Post currPost = posts.get(0);
				endDate = DateUtils.formatDate(currPost.getCreatedTime());
			}
		} catch (FacebookException e) {
			e.printStackTrace();
		}

		Hashtable<String, Object> toHash = getFanCount(pageId, DateUtils.formatDate(to), FilterOperator.LESS_THAN_OR_EQUAL, SortDirection.DESCENDING);
		long fanCount = (Long) toHash.get("like_count");
		long talkingAboutCount = (Long) toHash.get("talking_about_count");
		String baseInfoDate = (String) toHash.get("date");

		//SAVE OUTPUT TO DATASTORE
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity currEntity = new Entity(Constants.TASK_TABLE);
		currEntity.setProperty(Constants.ID_TRANSACTION_FIELD, idTranscation);
		currEntity.setProperty("pageId", pageId);

		currEntity.setProperty("endDate", endDate);
		currEntity.setProperty("fanCount", fanCount);
		currEntity.setProperty("talkingAboutCount", talkingAboutCount);
		currEntity.setProperty("baseInfoDate", baseInfoDate);

		datastore.put(currEntity);

	}


	private Hashtable<String, Object> getFanCount (String idPage, String date, FilterOperator fo, SortDirection sd) {

		Hashtable<String, Object> result = new Hashtable<String, Object>();
		result.put("like_count", 100L);
		result.put("talking_about_count", 100L);
		result.put("date", "ciao");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q;
		try {
			Filter fromFilter = new FilterPredicate("date", fo, DateUtils.parseDateAndTime(date));
			Filter idPageFilter = new FilterPredicate("idFacebook", FilterOperator.EQUAL, idPage);
			Filter compositeFilter = CompositeFilterOperator.and(idPageFilter, fromFilter);
			q = new Query(Constants.FACEBOOK_MONITOR_TABLE).setFilter(compositeFilter).addSort("date", sd);
			PreparedQuery pq = datastore.prepare(q);
			for (Entity ent : pq.asIterable()) {
				if ( ent.getProperty("like_count") != null) {
					result.put("like_count", (Long) ent.getProperty("like_count"));
					result.put("talking_about_count", (Long) ent.getProperty("talking_about_count"));
					result.put("date", DateUtils.formatDate(((Date) ent.getProperty("date"))));
					return result;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

}
