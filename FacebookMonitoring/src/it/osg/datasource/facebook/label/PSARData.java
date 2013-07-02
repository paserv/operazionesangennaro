package it.osg.datasource.facebook.label;

import facebook4j.Comment;
import facebook4j.Like;
import facebook4j.Post;
import it.osg.datasource.GraphSourceGenerator;
import it.osg.service.model.Graph;
import it.osg.utils.DateUtils;
import it.osg.utils.FacebookUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;


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
			ArrayList<Post> posts = FacebookUtils.getAllPosts(sindaco, f, t, null);
			
			//Media giornaliera dei nuovi Post pubblicati sulla fan page
			ArrayList<Post> postFromPage = new ArrayList<Post>();
			ArrayList<Post> postFromFan = new ArrayList<Post>();
			Iterator<Post> iterPost = posts.iterator();
			while (iterPost.hasNext()) {
				Post currPost = iterPost.next();
				if (currPost.getFrom().getId().equals(sindaco)) {
					postFromPage.add(currPost);
				} else {
					postFromFan.add(currPost);
				}
			}
			double numTotalePost = posts.size();
			double numTotalePostFromPage = postFromPage.size();
			double numTotalePostFromFan = postFromFan.size();
			
			Graph mediapost = new Graph("mediapost", numTotalePostFromPage/numGiorni);
			result.add(mediapost);
			
			//Totale nuovi Post pubblicati sulla fan page
			Graph totpost = new Graph("totpost", numTotalePostFromPage);
			result.add(totpost);
			
			//Totale nuovi Post pubblicati sulla page dai fan
			Graph totPostFromFan = new Graph("totPostFromFan", numTotalePostFromFan);
			result.add(totPostFromFan);
			
			//Media nuovi Post pubblicati sulla page dai fan
			Graph totPostFromFanMedio = new Graph("totPostFromFanMedio", numTotalePostFromFan/numGiorni);
			result.add(totPostFromFanMedio);
			
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
			
			/*
			 * PROVVISORIO
			 */
			Graph medialikes = new Graph("mediaNuoviFan", 0);
			result.add(medialikes);
			Graph totlikes = new Graph("totNuoviFanlikes", 0);
			result.add(totlikes);
			/*
			 * 
			 */
			
			//Totale fan della pagina facebook
			Hashtable<String, Object> baseInfo = FacebookUtils.getBaseInfo(sindaco);
			Graph totFan = new Graph("totFan", Double.valueOf((String) baseInfo.get("likes")));
			result.add(totFan);
			
			//Totale talking about della pagina facebook
			Graph talkAbout = new Graph("talkAbout", Double.valueOf((String) baseInfo.get("talking_about_count")));
			result.add(talkAbout);
			
			//Numero Commenti ai post nell'intervallo
			ArrayList<Comment> comments = FacebookUtils.getComments(postFromPage);
			Graph commentCount = new Graph("commentCount", comments.size());
			result.add(commentCount);
			
			//Numero medio Commenti per Post nell'intervallo
			Graph commentsPerPost = new Graph("commentsPerPost", comments.size()/numTotalePostFromPage);
			result.add(commentsPerPost);
			
			//Numero Unique Authors dei commenti nell'intervallo
			ArrayList<String> uniqueAuth = FacebookUtils.getUniqueAuthors(comments);
			Graph uniqueAuthors = new Graph("uniqueAuthors", uniqueAuth.size());
			result.add(uniqueAuthors);
			
			//Unique Authors che in media commentano un contenuto pubblicato sulla fan page
			Graph uniqueAuthorsPerPost = new Graph("uniqueAuthorsPerPost", uniqueAuth.size()/numTotalePostFromPage);
			result.add(uniqueAuthorsPerPost);
			
			//Media Likes per Post nell'intervallo
			ArrayList<Like> likes = FacebookUtils.getLikes(postFromPage);
			Graph mediaLikePerPost = new Graph("mediaLikePerPost", likes.size()/numTotalePostFromPage);
			result.add(mediaLikePerPost);
			
			//Totale Likes nell'intervallo
			Graph totLikes = new Graph("totLikes", likes.size());
			result.add(totLikes);
			
			//Media Shares per Post nell'intervallo
			double totShares = FacebookUtils.getShares(postFromPage);
			Graph sharesPerPost = new Graph("sharesPerPost", totShares/numTotalePostFromPage);
			result.add(sharesPerPost);
			
			//Totale Shares per Post nell'intervallo
			Graph totShare = new Graph("totShares", totShares);
			result.add(totShare);
			
			//Numero medio Commenti per Autore
			double commentsPerAuthor = 0;
			if (uniqueAuth.size() != 0 && comments.size() != 0) {
				double com = Double.valueOf(String.valueOf(comments.size()));
				double aut = Double.valueOf(String.valueOf(uniqueAuth.size()));
				commentsPerAuthor = com/aut;
			}
			Graph commentPerAuthor = new Graph("commentsPerAuthor", commentsPerAuthor);
			result.add(commentPerAuthor);
			
			//TODO Numero medio delle Risposte ai Commenti
			//TODO Tempo medio delle Risposte ai Commenti
			
			Graph numGio = new Graph("numGiorni", numGiorni);
			result.add(numGio);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;


	}






}
