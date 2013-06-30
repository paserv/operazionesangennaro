package it.osg.datasource.facebook.label;

import facebook4j.Comment;
import facebook4j.Post;
import it.osg.datasource.GraphSourceGenerator;
import it.osg.service.model.Graph;
import it.osg.utils.ArrayUtils;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.util.ArrayUtil;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;


public class PSARData extends GraphSourceGenerator {


	@Override
	public ArrayList<Graph> getGraphData(Object[] objects) {

		/*
		 * [0] -> sindaco
		 * [1] -> from
		 * [2] -> to
		 */
		ArrayList<Graph> result = new ArrayList<Graph>();

		//Dati di input
		String sindaco = (String) objects[0];
		Date f = null;
		Date t = null;
		try {
			if (objects[1] != null && objects[2] != null){
				f = DateUtils.parseDateAndTime((String) objects[1]);
				t = DateUtils.parseDateAndTime((String)objects[2]);
			}

			double numGiorni = DateUtils.giorniTraDueDate(f, t);

			//Dati di Output
			//Media giornaliera dei nuovi Post pubblicati sulla fan page
			ArrayList<Post> posts = FacebookUtils.getAllPosts(sindaco, f, t, new String[]{"id", "created_time, comments"});
			double numTotalePost = posts.size();
			Graph mediapost = new Graph("mediapost", numTotalePost/numGiorni);
			result.add(mediapost);
			
			//Totale nuovi Post pubblicati sulla fan page
			Graph totpost = new Graph("totpost", numTotalePost);
			result.add(totpost);
			
			/*
			//Media giornaliera nuovi fan della pagina facebook
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Query q;
			PreparedQuery pq;
			
			Filter fromFilter = new FilterPredicate("date", FilterOperator.GREATER_THAN_OR_EQUAL, f);
			q = new Query((String)objects[0]).setFilter(fromFilter).addSort("date", SortDirection.ASCENDING);
			pq = datastore.prepare(q);
			List<Entity> resultFrom = pq.asList(FetchOptions.Builder.withLimit(1));
			long likesFrom = 0;
			if (resultFrom.size() > 0) {
				likesFrom = (Long) resultFrom.get(0).getProperty("like_count");
			}
			
			Filter toFilter = new FilterPredicate("date", FilterOperator.LESS_THAN_OR_EQUAL, t);
			q = new Query((String)objects[0]).setFilter(toFilter).addSort("date", SortDirection.DESCENDING);
			pq = datastore.prepare(q);
			List<Entity> resultTo = pq.asList(FetchOptions.Builder.withLimit(1));
			long likesTo = 0;
			if (resultTo.size() > 0) {
				likesTo = (Long) resultTo.get(0).getProperty("like_count");
			}
			 
			double numNuoviLikes = likesTo - likesFrom;
			Graph medialikes = new Graph("medialikes", numNuoviLikes/numGiorni);
			result.add(medialikes);
			
			//Totale nuovi fan della pagina facebook
			Graph totlikes = new Graph("totlikes", numNuoviLikes);
			result.add(totlikes);
			 */
			
			
			//Numero Commenti ai post nell'intervallo
			ArrayList<Comment> comments = FacebookUtils.getComments(posts, f, t);
			Graph commentCount = new Graph("commentCount", comments.size());
			result.add(commentCount);
			
			//Numero Unique Authors dei commenti nell'intervallo
			ArrayList<String> uniqueAuth = FacebookUtils.getUniqueAuthors(comments, f, t);
			Graph uniqueAuthors = new Graph("uniqueAuthors", uniqueAuth.size());
			result.add(uniqueAuthors);
			
			//Unique Authors che in media commentano un contenuto pubblicato sulla fan page
			Graph uniqueAuthorsPerPost = new Graph("uniqueAuthorsPerPost", uniqueAuth.size()/numTotalePost);
			result.add(uniqueAuthorsPerPost);
			
			//TODO Media Likes per Post
			
			
			
			Graph numGio = new Graph("numGiorni", numGiorni);
			result.add(numGio);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;


	}






}
