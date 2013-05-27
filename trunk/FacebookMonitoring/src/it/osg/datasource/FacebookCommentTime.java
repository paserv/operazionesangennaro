package it.osg.datasource;

import facebook4j.Comment;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.IdNameEntity;
import facebook4j.PagableList;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import it.osg.service.model.Graph;
import it.osg.utils.DateUtils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/* 
 * Preleva i dati direttamente da Facebook ed in base ai dati che si vuole tirar fuori restituisce un ArrayList<GraphData>
 */

public class FacebookCommentTime extends SourceGenerator {


	@Override
	public ArrayList<Graph> getGraphData(Object[] objects) {
		ArrayList<Graph> result = new ArrayList<Graph>();

		Date f = null;
		Date t = null;
		try {
			if (objects[0] != null && objects[1] != null){
				f = DateUtils.parseDateAndTime((String) objects[1]);
				t = DateUtils.parseDateAndTime((String)objects[2]);
			}

			Date currFrom = new Date(f.getTime());
			Date currTo = new Date(t.getTime());

			Facebook facebook = new FacebookFactory().getInstance();
			facebook.setOAuthAppId("156346967866710", "e0f880cc248e811c98952d9a44a27ce4");
			//facebook.setOAuthPermissions(commaSeparetedPermissions);
			facebook.setOAuthAccessToken(new AccessToken("156346967866710|gnswdSXw_ObP0RaWj5qqgK_HtCk", null));

			try {
				//"166115370094396"
				ResponseList<Post> facResults = facebook.getFeed((String)objects[0], new Reading().since(currFrom).until(currTo).limit(100));

				Iterator<Post> iter = facResults.iterator();
				while (iter.hasNext()){
					Post curr = iter.next();
					String currId = curr.getId();

//					PagableList<Comment> comments = curr.getComments();
//					Long commentCount = Long.valueOf(String.valueOf(comments.size()));

					PagableList<IdNameEntity> commentCount = curr.getLikes();
					long ord = Long.valueOf(String.valueOf(commentCount.size()));
					
					Graph gd = new Graph(currId, ord);
					result.add(gd);

				}

			} catch (FacebookException e) {
				e.printStackTrace();
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}


}
