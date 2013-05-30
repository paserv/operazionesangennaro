package it.osg.datasource.facebook.time;

import it.osg.datasource.SourceGenerator;
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

public class DPickedData extends SourceGenerator {


	@Override
	public ArrayList<Graph> getGraphData(Object[] objects) {

		ArrayList<Graph> result = new ArrayList<Graph>();
		
		ArrayList<Graph> likes = new ArrayList<Graph>();
		PickedData myLikes = new PickedData();
		likes = myLikes.getGraphData(objects);
		
		for (int i = 0; i < likes.size() - 1; i++) {
			 Graph currGraph = likes.get(i);
			 Graph currGraph2 = likes.get(i + 1);
			 Graph newGraph = new Graph(currGraph2.getAxis(), currGraph2.getOrdinate() - currGraph.getOrdinate());
			 result.add(newGraph);
		}
		

		return result;

	}


}
