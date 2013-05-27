package it.osg.utils;

import it.osg.service.model.TransmissionData;

import java.util.ArrayList;
import java.util.Date;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;

public class DataStoreAO {

	public static ArrayList<TransmissionData> getTransmissionData(String transmission, Date from, Date to) {
		ArrayList<TransmissionData> result = new ArrayList<TransmissionData>();

		// Get the Datastore Service
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Filter fromFilter = new FilterPredicate("date", FilterOperator.GREATER_THAN_OR_EQUAL, from);
		Filter toFilter = new FilterPredicate("date", FilterOperator.LESS_THAN_OR_EQUAL, to);
		Filter fromToFilter = CompositeFilterOperator.and(fromFilter, toFilter);
		Query q = new Query(transmission).setFilter(fromToFilter).addSort("date", SortDirection.ASCENDING);
		//Query q = new Query(transmission).addSort("date", SortDirection.ASCENDING);
		
		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);

		for (Entity ent : pq.asIterable()) {
			
			Date date = new Date();
			
			Object dt = ent.getProperty("date");
			if (dt instanceof Date) {
				date = (Date) dt;
			} else {
								
			}
			
			long likeCount = 0L;
			
			Object lk = ent.getProperty("like_count");
			if (lk instanceof Long) {
				likeCount = (Long) lk;
			} else {
				likeCount = Long.valueOf((String) lk);
			}
			
			long talkingAboutCount = 0L;
			Object tk = ent.getProperty("talking_about_count");
			if (tk instanceof Long) {
				talkingAboutCount = (Long) tk;
			} else {
				talkingAboutCount = Long.valueOf((String) tk);
			}
					
			
			long timestamp = 0L;
			Object tm = ent.getProperty("timestamp");
			if (tm instanceof Long) {
				timestamp = (Long) tm;
			} else {
				timestamp = Long.valueOf((String) tm);
			}
			
			Key key = ent.getKey();
			
			TransmissionData curr = new TransmissionData();
			curr.setDate(date);
			curr.setId(key.getName());
			curr.setDatastoreId(key);
			curr.setLikeCount(likeCount);
			curr.setTalkingAboutCount(talkingAboutCount);
			curr.setTimestamp(timestamp);
			result.add(curr);
			System.out.println(curr.toString());


		}

		return result;
	}
	
}