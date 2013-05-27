package it.osg.datasource;

import it.osg.service.model.Graph;
import it.osg.utils.DateUtils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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

/* 
 * Preleva i dati direttamente da Facebook ed in base ai dati che si vuole tirar fuori restituisce un ArrayList<GraphData>
 */

public class FacebookLikeTime extends SourceGenerator {


	@Override
	public ArrayList<Graph> getGraphData(Object[] objects) {

		ArrayList<Graph> result = new ArrayList<Graph>();

		Date f = null;
		Date t = null;
		try {
			if (objects[0] != null && objects[1] != null) {
				f = DateUtils.parseDateAndTime((String) objects[1]);
				t = DateUtils.parseDateAndTime((String)objects[2]);
			}

			// Get the Datastore Service
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Query q;
			if (f != null && t != null) {
				Filter fromFilter = new FilterPredicate("date", FilterOperator.GREATER_THAN_OR_EQUAL, f);
				Filter toFilter = new FilterPredicate("date", FilterOperator.LESS_THAN_OR_EQUAL, t);
				Filter fromToFilter = CompositeFilterOperator.and(fromFilter, toFilter);
				q = new Query((String)objects[0]).setFilter(fromToFilter).addSort("date", SortDirection.ASCENDING);
			} else {
				q = new Query((String)objects[0]).addSort("date", SortDirection.ASCENDING);
			}


			// Use PreparedQuery interface to retrieve results
			PreparedQuery pq = datastore.prepare(q);

			for (Entity ent : pq.asIterable()) {

				String axisReturned = "";
				Long ordinateReturned = 0L;

				Object datastoreAxis = ent.getProperty((String) objects[3]);
				if (datastoreAxis instanceof Date) {
					axisReturned = DateUtils.formatDateAndTime((Date) datastoreAxis);
				}

				Object datastoreOrdinate = ent.getProperty((String) objects[4]);
				if (datastoreOrdinate instanceof Long) {
					ordinateReturned = (Long) datastoreOrdinate;
				}

				Graph gd = new Graph(axisReturned, ordinateReturned);
				result.add(gd);
				System.out.println(gd.toString());

			} 
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;

	}


}
